package cn.xianbin.commonapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConditionDto {
    @ApiModelProperty(value = "字段", required = true)
    private String filed;
    @ApiModelProperty(value = "数据类型", required = true)
    private String type;
    @ApiModelProperty(value = "表达式", required = true)
    private String express;
    @ApiModelProperty(value = "匹配规则", required = true)
    private String match;
}
