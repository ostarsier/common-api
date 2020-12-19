package cn.xianbin.commonapi.service.impl;

import cn.xianbin.commonapi.dto.Page;
import cn.xianbin.commonapi.dto.WhereDto;
import cn.xianbin.commonapi.entity.CommonApiConfig;
import cn.xianbin.commonapi.entity.CommonApiDatasource;
import cn.xianbin.commonapi.entity.CommonApiExcLog;
import cn.xianbin.commonapi.entity.CommonApiLog;
import cn.xianbin.commonapi.enums.DataTypeEnum;
import cn.xianbin.commonapi.enums.JdbcPagingEnum;
import cn.xianbin.commonapi.enums.SqlOperator;
import cn.xianbin.commonapi.exception.ServerException;
import cn.xianbin.commonapi.service.ICommonApiExcLogService;
import cn.xianbin.commonapi.service.ICommonApiLogService;
import cn.xianbin.commonapi.service.ICommonApiService;
import cn.xianbin.commonapi.util.DataSourceUtil;
import cn.xianbin.commonapi.util.PlatformUtil;
import cn.xianbin.commonapi.vo.JdbcSourceInfo;
import cn.xianbin.commonapi.vo.WhereConfig;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class CommonApiService implements ICommonApiService {
    @Autowired
    private ICommonApiLogService apiLogService;
    @Autowired
    private ICommonApiExcLogService commonApiExcLogService;

    /**
     * @param params
     * @param commonApiConfig
     * @param commonApiDatasource
     * @param page
     * @param request
     * @return
     */
    @Override
    public void getData(String params, CommonApiConfig commonApiConfig, CommonApiDatasource commonApiDatasource, Page<?> page, HttpServletRequest request) {
        JdbcSourceInfo jdbcSourceInfo = DataSourceUtil.getJdbcSourceInfo(commonApiDatasource);
        DruidDataSource dataSource = DataSourceUtil.getDataSource(jdbcSourceInfo);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = buildSql(commonApiConfig.getSqlTxt(), commonApiConfig.getWhereTxt(), params, page);
        String countSql = buildCountSql(sql);
        String querySql = buildPagingSql(sql, commonApiDatasource, page);
        try {
            log.info("执行的SQL:\n【{}】", querySql);
            Integer size = jdbcTemplate.queryForObject(countSql, Integer.class);
            page.setTotal(size);
            page.setRecords(jdbcTemplate.queryForList(querySql));
        } catch (BadSqlGrammarException bsge) {
            log.error("接口{}查询数据失败", commonApiConfig.getApiCode());
            try {
                List<CommonApiExcLog> excLogs = commonApiExcLogService.selectByMsg(commonApiConfig.getApiCode(), bsge.getMessage());
                //记录接口异常日志
                if (excLogs.isEmpty()) {
                    CommonApiExcLog excLog = new CommonApiExcLog();
                    excLog.setApiCode(commonApiConfig.getApiCode());
                    excLog.setExcParam(params);
                    excLog.setExcCount(1);
                    excLog.setExcTime(new Date());
                    excLog.setExcMsg(bsge.getMessage());
                    excLog.setExcSql(bsge.getSql());
                    commonApiExcLogService.save(excLog);
                }
            } catch (Exception e) {
                log.error("记录接口异常日志失败：{}", e.getMessage());
            }
            bsge.printStackTrace();
            throw new ServerException("接口执行失败！");
        } finally {
            CommonApiLog log = new CommonApiLog();
            log.setIp(PlatformUtil.getIPByrequest(request));
            log.setApiName(commonApiConfig.getApiName());
            log.setVisitTime(new Date());
            apiLogService.save(log);
        }
    }

    /**
     * 获取
     *
     * @param whereTxt
     * @return
     */
    @Override
    public List<WhereDto> getParams(String whereTxt) {
        if (StringUtils.isEmpty(whereTxt)) {
            return null;
        }
        List<WhereDto> params = new ArrayList<>();
        try {
            JSONObject jo = JSONObject.parseObject(whereTxt);
            for (String key : jo.keySet()) {
                WhereConfig config = jo.getObject(key, WhereConfig.class);
                WhereDto dto = new WhereDto();
                dto.setParam(key);
                dto.setType(config.getType());
                params.add(dto);
            }
        } catch (Exception e) {
            log.error("参数格式异常");
            e.printStackTrace();
        }
        return params;
    }

    /**
     * @param sqlTxt   标准sql(例如: select name from person where [$age])
     * @param whereTxt 参数模板(例如: {"age":{"express":"[$age]","filed":"age","match":">","type":"数字"}})
     * @param params   参数(例如: {"age":20})
     */
    private String buildSql(String sqlTxt, String whereTxt, String params, Page<?> page) {
        try {
            String sql = sqlTxt;
            JSONObject whereConfigList = JSONObject.parseObject(whereTxt.trim());
            JSONObject paramsJson = JSONObject.parseObject(params);
            for (String key : whereConfigList.keySet()) {
                WhereConfig whereConfig = whereConfigList.getObject(key, WhereConfig.class);
                String paramValue = paramsJson.getString(key);
                if (!StringUtils.isEmpty(paramValue)) {
                    DataTypeEnum awe = DataTypeEnum.getInstance(whereConfig.getType());
                    if (awe == null) {
                        throw new ServerException("参数格式配置异常");
                    }
                    StringBuilder where = new StringBuilder();
                    where.append(" ");
                    where.append("and");
                    where.append(" ");
                    where.append(whereConfig.getFiled());
                    where.append(" ");
                    where.append(whereConfig.getMatch());
                    switch (awe.getVal()) {
                        case "字符":
                            where.append(awe.getPrefix());
                            if (SqlOperator.LIKE.getVal().equals(whereConfig.getMatch())) {
                                where.append("%");
                            }
                            where.append(paramValue);
                            if (SqlOperator.LIKE.getVal().equals(whereConfig.getMatch())) {
                                where.append("%");
                            }
                            where.append(awe.getPrefix());
                            break;
                        case "数字":
                            where.append(awe.getPrefix());
                            where.append(paramValue);
                            where.append(awe.getPrefix());
                            break;
                        case "数组":
                            where.append("(");
                            where.append("''");
                            JSONArray ja = paramsJson.getJSONArray(key);
                            for (Object o : ja) {
                                where.append(",");
                                where.append(awe.getPrefix());
                                where.append(o);
                                where.append(awe.getPrefix());
                            }
                            where.append(")");
                            break;
                        case "日期":
                            where.append("date_format(");
                            where.append(awe.getPrefix());
                            where.append(paramValue);
                            where.append(awe.getPrefix());
                            where.append(",");
                            where.append(awe.getPrefix());
                            where.append(awe.getFormat());
                            where.append(awe.getPrefix());
                            where.append(")");
                            break;
                        default:
                            throw new ServerException("参数格式配置异常");
                    }
                    where.append(" ");
                    sql = sql.replace(whereConfig.getExpress(), where.toString());
                } else {
                    sql = sql.replace(whereConfig.getExpress(), "");
                }
            }

            return sql;
            //            return buildPagingSql(sql, jpe, currentPage, pageSize);
        } catch (Exception e) {
            log.error("sql解析失败:{}", sqlTxt);
            log.error(e.getMessage(), e);
            throw new ServerException("sql异常，解析失败：" + e.getMessage());
        }
    }

    private String buildCountSql(String sql) {
        return "select count(1) from (" + sql + ") t";
    }

    private String buildPagingSql(String sql, CommonApiDatasource commonApiDatasource, Page<?> page) {
        JdbcPagingEnum jpe = JdbcPagingEnum.getInstance(commonApiDatasource.getType());
        if (null == jpe) {
            throw new ServerException("数据源类型异常");
        }
        Integer from = (page.getCurrentPage() - 1) * page.getPageSize();
        return sql + jpe.getPaging().replace("[$from]", from + "").replace("[$pageSize]", page.getPageSize() + "");
    }
}
