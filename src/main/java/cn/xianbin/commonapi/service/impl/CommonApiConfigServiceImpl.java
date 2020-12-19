package cn.xianbin.commonapi.service.impl;

import cn.xianbin.commonapi.common.QueryParams;
import cn.xianbin.commonapi.dto.ApiConfigAddDto;
import cn.xianbin.commonapi.dto.ApiConfigDto;
import cn.xianbin.commonapi.dto.ConditionDto;
import cn.xianbin.commonapi.entity.CommonApiConfig;
import cn.xianbin.commonapi.entity.CommonApiDatasource;
import cn.xianbin.commonapi.exception.ServerException;
import cn.xianbin.commonapi.mapper.CommonApiConfigMapper;
import cn.xianbin.commonapi.service.ICommonApiConfigService;
import cn.xianbin.commonapi.service.ICommonApiDatasourceService;
import cn.xianbin.commonapi.util.DataSourceUtil;
import cn.xianbin.commonapi.vo.JdbcSourceInfo;
import cn.xianbin.commonapi.vo.WhereConfig;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CommonApiConfigServiceImpl extends ServiceImpl<CommonApiConfigMapper, CommonApiConfig> implements ICommonApiConfigService {
    @Autowired
    private CommonApiConfigMapper commonApiConfigDao;
    @Autowired
    private ICommonApiDatasourceService commonApiDatasourceService;

    @Override
    public CommonApiConfig getConfigByApiCode(String apiCode) {
        return commonApiConfigDao.getConfigByApiCode(apiCode);
    }

    @Override
    public ApiConfigDto selectByApiCode(String apiCode) {
        CommonApiConfig api = commonApiConfigDao.getConfigByApiCode(apiCode);
        if (null == api) {
            throw new ServerException("无效的接口编码");
        }
        CommonApiDatasource datasource = commonApiDatasourceService.selectById(api.getDsId());
        if (null == datasource) {
            throw new ServerException("无效的数据源id");
        }
        ApiConfigDto dto = new ApiConfigDto();
        BeanUtils.copyProperties(api, dto);
        dto.setSql(api.getSqlTxt());
        Map<String, ConditionDto> conditionMap = new HashMap<>();
        JSONObject conditionJson = JSONObject.parseObject(api.getWhereTxt());
        for (String key : conditionJson.keySet()) {
            conditionMap.put(key, JSONObject.parseObject(conditionJson.getString(key), ConditionDto.class));
        }
        dto.setCondition(conditionMap);
        dto.setDataSourceId(api.getDsId());
        dto.setDataSourceName(datasource.getName());
        if (!StringUtils.isEmpty(api.getFiledNote())) {
            dto.setFiledNote(JSONObject.parseObject(api.getFiledNote(), HashMap.class));
        }

        return dto;
    }

    @Override
    public int insert(ApiConfigAddDto srcDto) {
        ApiConfigDto dto = new ApiConfigDto();
        BeanUtils.copyProperties(srcDto, dto);
        CommonApiConfig apiConfig = commonApiConfigDao.getConfigByApiCode(dto.getApiCode());
        if (null != apiConfig) {
            throw new ServerException("重复的接口编码");
        }
        apiConfig = new CommonApiConfig();
        invalidDataSource(dto);
        BeanUtils.copyProperties(dto, apiConfig);
        apiConfig.setDsId(dto.getDataSourceId());
        apiConfig.setSqlTxt(dto.getSql());
        apiConfig.setWhereTxt(JSONObject.toJSONString(dto.getCondition()));
        apiConfig.setFiledNote(JSONObject.toJSONString(dto.getFiledNote()));
        apiConfig.setCreateTime(new Date());
        return commonApiConfigDao.insert(apiConfig);
    }

    @Override
    public int update(ApiConfigDto dto) {
        invalidDataSource(dto);
        CommonApiConfig apiConfig = new CommonApiConfig();
        BeanUtils.copyProperties(dto, apiConfig);
        apiConfig.setDsId(dto.getDataSourceId());
        apiConfig.setSqlTxt(dto.getSql());
        apiConfig.setWhereTxt(JSONObject.toJSONString(dto.getCondition()));
        apiConfig.setFiledNote(JSONObject.toJSONString(dto.getFiledNote()));
        apiConfig.setUpdateTime(new Date());
        return commonApiConfigDao.updateById(apiConfig);
    }

    @Override
    public int delete(int id) {
        return commonApiConfigDao.deleteById(id);
    }

    private String buildSql(ApiConfigDto dto) {
        try {
            StringBuilder builder = new StringBuilder("select * from (");
            String sql = dto.getSql();
            JSONObject paramJson = JSONObject.parseObject(JSONObject.toJSONString(dto.getCondition()).trim());
            for (String key : paramJson.keySet()) {
                WhereConfig config = paramJson.getObject(key, WhereConfig.class);
                sql = sql.replace(config.getExpress(), "");
            }
            builder.append(sql).append(") ").append("table_").append(System.currentTimeMillis()).append(" limit 0,1");
            return builder.toString();
        } catch (Exception e) {
            log.error("sql解析失败:{}", dto.getSql());
            e.printStackTrace();
            throw new ServerException("sql异常，解析失败：" + dto.getSql());
        }
    }

    private Boolean invalidId(String id) {
        return null == id;
    }

    /**
     * 验证sql是否合法
     *
     * @param dto
     */
    private void invalidDataSource(ApiConfigDto dto) {
        if (invalidId(dto.getDataSourceId())) {
            throw new ServerException("数据源id不能为空");
        }
        CommonApiDatasource datasource = commonApiDatasourceService.selectById(dto.getDataSourceId());
        if (null == datasource) {
            throw new ServerException("无效的数据源id");
        }
        //验证sql是否正确
        JdbcSourceInfo jdbcSourceInfo = DataSourceUtil.getJdbcSourceInfo(datasource);
        DruidDataSource jdbcDataSource = DataSourceUtil.getDataSource(jdbcSourceInfo);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(jdbcDataSource);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(buildSql(dto));

        SqlRowSetMetaData rowSetMetaData = sqlRowSet.getMetaData();
        StringBuilder columnCheckRet = new StringBuilder();
        int columnCount = rowSetMetaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnLabel = rowSetMetaData.getColumnLabel(i);
            if (!dto.getFiledNote().containsKey(columnLabel)) {
                columnCheckRet.append("、");
                columnCheckRet.append(columnLabel);
            }
        }
        if (columnCheckRet.length() > 0) {
            throw new ServerException("字段" + columnCheckRet.substring(1, columnCheckRet.length()) + "未配置中文注释");
        }
    }

    public IPage<CommonApiConfig> pageList(String keyword) {
        Page page = QueryParams.getPage();
        LambdaQueryWrapper<CommonApiConfig> wrapper = new QueryWrapper().lambda();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(keyword)) {
            wrapper.like(CommonApiConfig::getApiName, keyword);
        }

        wrapper.orderByDesc(CommonApiConfig::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }
}
