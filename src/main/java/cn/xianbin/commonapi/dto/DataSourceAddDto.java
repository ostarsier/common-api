package cn.xianbin.commonapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataSourceAddDto {
    @ApiModelProperty(value = "名称", required = true)
    private String name;
    @ApiModelProperty(value = "数据库地址", required = true)
    private String url;
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    @ApiModelProperty(value = "密码", required = true)
    private String password;
    @ApiModelProperty(value = "数据库类型", required = true)
    private String type;
}
