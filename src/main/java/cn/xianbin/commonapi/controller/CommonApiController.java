package cn.xianbin.commonapi.controller;

import cn.xianbin.commonapi.common.QueryParams;
import cn.xianbin.commonapi.dto.*;
import cn.xianbin.commonapi.entity.CommonApiConfig;
import cn.xianbin.commonapi.entity.CommonApiDatasource;
import cn.xianbin.commonapi.enums.DataBaseTypeEnum;
import cn.xianbin.commonapi.enums.DataTypeEnum;
import cn.xianbin.commonapi.enums.SqlOperator;
import cn.xianbin.commonapi.rest.Resp;
import cn.xianbin.commonapi.service.ICommonApiConfigService;
import cn.xianbin.commonapi.service.ICommonApiDatasourceService;
import cn.xianbin.commonapi.service.ICommonApiService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@Api(value = "数据接口", tags = "数据接口")
public class CommonApiController extends BaseController {
    @Autowired
    private ICommonApiConfigService commonApiConfigService;
    @Autowired
    private ICommonApiDatasourceService commonApiDatasourceService;
    @Autowired
    private ICommonApiService commonApiService;

    @GetMapping("/enums/{type}")
    @ApiOperation(value = "枚举类型")
    public Resp<List<String>> addConfig(@PathVariable String type) {
        switch (type) {
            case "dataType":
                return Resp.success(DataTypeEnum.keys());
            case "databaseType":
                return Resp.success(DataBaseTypeEnum.keys());
            case "sqlOperator":
                return Resp.success(SqlOperator.keys());
        }
        return Resp.failed();
    }


    @ApiOperation(value = "数据查询接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNo", value = "页码", type = "query", dataType = "int", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", type = "query", dataType = "int", example = "10")
    })
    @PostMapping("/api/data/{apiCode}")
    public Resp<?> getData(@PathVariable String apiCode, @RequestBody(required = false) JSONObject params, HttpServletRequest request) {
        if (params == null) {
            params = new JSONObject();
        }

        if (invalidString(apiCode)) {
            return Resp.failed("接口编码不能为空");
        }
        CommonApiConfig commonApiConfig = commonApiConfigService.getConfigByApiCode(apiCode);
        if (null == commonApiConfig) {
            return Resp.failed("apiCode无匹配接口");
        }
        CommonApiDatasource dataSourceInfo = commonApiDatasourceService.selectById(commonApiConfig.getDsId());
        if (null == dataSourceInfo) {
            return Resp.failed("apiCode对应接口数据源异常");
        }
        Page<?> page = new Page<>();
        page.setCurrentPage((int) QueryParams.getPage().getCurrent());
        page.setPageSize((int) QueryParams.getPage().getSize());

        commonApiService.getData(params.toJSONString(), commonApiConfig, dataSourceInfo, page, request);
        return Resp.success(page);
    }

    @ApiOperation(value = "查询接口参数接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "apiCode", value = "接口编码", required = true, type = "query", dataType = "string", example = "davinci-schdule")
    })
    @GetMapping("/api/data")
    public Resp<?> getParams(String apiCode) {
        if (invalidString(apiCode)) {
            return Resp.failed("接口编码不能为空");
        }
        CommonApiConfig commonApiConfig = commonApiConfigService.getConfigByApiCode(apiCode);
        if (null == commonApiConfig) {
            return Resp.failed("apiCode无匹配接口");
        }
        CommonApiDatasource dataSourceInfo = commonApiDatasourceService.selectById(commonApiConfig.getDsId());
        if (null == dataSourceInfo) {
            return Resp.failed("apiCode对应接口数据源异常");
        }
        return Resp.success(commonApiService.getParams(commonApiConfig.getWhereTxt()));
    }

    @ApiOperation(value = "查询接口返回结果模板")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "apiCode", value = "接口编码", required = true, type = "query", dataType = "string", example = "davinci-schdule")
    })
    @GetMapping("/api/resultTemplate")
    public Resp<HashMap> getResult(String apiCode) {
        if (invalidString(apiCode)) {
            return Resp.failed("接口编码不能为空");
        }
        CommonApiConfig commonApiConfig = commonApiConfigService.getConfigByApiCode(apiCode);
        if (null == commonApiConfig) {
            return Resp.failed("apiCode无匹配接口");
        }
        CommonApiDatasource dataSourceInfo = commonApiDatasourceService.selectById(commonApiConfig.getDsId());
        if (null == dataSourceInfo) {
            return Resp.failed("apiCode对应接口数据源异常");
        }
        HashMap resultTemplate = JSONObject.parseObject(commonApiConfig.getFiledNote(), HashMap.class);
        return Resp.success(resultTemplate);
    }

    @PutMapping("/commonApiConfig/add")
    @ApiOperation(value = "新增接口")
    @ApiImplicitParam(name = "api", value = "接口信息", type = "body", dataType = "ApiConfigAddDto", required = true)
    public Resp<?> addConfig(@RequestBody ApiConfigAddDto api) {
        if (null == api) {
            return Resp.failed("接口不能为空");
        }
        if (commonApiConfigService.insert(api) >= 1) {
            return Resp.success();
        } else {
            return Resp.failed("新增失败");
        }
    }

    @GetMapping("/commonApiConfig/get")
    @ApiOperation(value = "查询接口配置")
    @ApiImplicitParam(name = "apiCode", value = "接口编码", type = "query", dataType = "string", required = true)
    public Resp<ApiConfigDto> getConfig(String apiCode) {
        if (StringUtils.isEmpty(apiCode)) {
            return Resp.failed("数据源名称不能为空");
        }
        ApiConfigDto dto = commonApiConfigService.selectByApiCode(apiCode);
        return Resp.success(dto);
    }

    @GetMapping("/commonApiConfig/pageList")
    @ApiOperation(value = "分页查询接口配置")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNo", value = "页数", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页大小", required = true, dataType = "String"),
    })
    public Resp<IPage<CommonApiConfig>> list(@RequestParam(required = false) String keyword) {
        IPage<CommonApiConfig> pageList = commonApiConfigService.pageList(keyword);
        return Resp.success(pageList);
    }

    @PostMapping("/commonApiConfig/update")
    @ApiOperation(value = "更新接口配置")
    @ApiImplicitParam(name = "api", value = "数据源", type = "body", dataType = "ApiConfigDto", required = true)
    public Resp<?> updateConfig(@RequestBody ApiConfigDto api) {
        if (null == api) {
            return Resp.failed("接口不能为空");
        }
        if (commonApiConfigService.update(api) >= 1) {
            return Resp.success();
        } else {
            return Resp.failed("更新失败");
        }
    }

    @PutMapping("/commonApiDatasource/add")
    @ApiOperation(value = "新增数据源")
    @ApiImplicitParam(name = "dataSource", value = "数据源", type = "body", dataType = "DataSourceAddDto", required = true)
    public Resp<?> addDataSource(@RequestBody DataSourceAddDto dataSource) {
        if (null == dataSource) {
            return Resp.failed("数据源不能为空");
        }
        if (commonApiDatasourceService.insert(dataSource) >= 1) {
            return Resp.success();
        } else {
            return Resp.failed("新增失败");
        }

    }

    @GetMapping("/commonApiDatasource/pageList")
    @ApiOperation(value = "分页查询数据源")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "keyword", value = "名称", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNo", value = "页数", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "一页大小", required = true, dataType = "String"),
    })
    public Resp<IPage<CommonApiDatasource>> dataSourcePageList(@RequestParam(required = false) String keyword) {
        IPage<CommonApiDatasource> pageList = commonApiDatasourceService.pageList(keyword);
        pageList.getRecords().forEach(datasource -> datasource.setPassword(null));
        return Resp.success(pageList);
    }

    @GetMapping("/commonApiDatasource/get")
    @ApiOperation(value = "查询数据源")
    @ApiImplicitParam(name = "name", value = "数据源名称", type = "query", dataType = "string", required = true)
    public Resp<List<DataSourceDto>> getDataSource(String name) {
        List<DataSourceDto> dataSources = commonApiDatasourceService.selectByName(name);
        return Resp.success(dataSources);
    }

    @PostMapping("/commonApiDatasource/update")
    @ApiOperation(value = "更新数据源")
    @ApiImplicitParam(name = "dataSource", value = "数据源", type = "body", dataType = "DataSourceDto", required = true)
    public Resp<?> updateDataSource(@RequestBody DataSourceDto dataSource) {
        if (null == dataSource) {
            return Resp.failed("数据源不能为空");
        }
        if (commonApiDatasourceService.update(dataSource) >= 1) {
            return Resp.success();
        } else {
            return Resp.failed("更新失败");
        }
    }
}
