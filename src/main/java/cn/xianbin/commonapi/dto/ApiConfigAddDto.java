package cn.xianbin.commonapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ApiConfigAddDto {
    @ApiModelProperty(value = "接口编码", required = true)
    private String apiCode;
    @ApiModelProperty(value = "接口名称", required = true)
    private String apiName;
    @ApiModelProperty(value = "数据源ID", required = true)
    private String dataSourceId;
    @ApiModelProperty(value = "查询SQL", required = true)
    private String sql;
    @ApiModelProperty(value = "查询条件", required = false)
    private Map<String, ConditionDto> condition;
    @ApiModelProperty(value = "查询结果字段对应中文备注", required = true)
    private Map<String, String> filedNote;
}
