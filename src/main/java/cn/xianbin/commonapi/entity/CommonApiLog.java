package cn.xianbin.commonapi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommonApiLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "ip地址")
    @TableField("ip")
    private String ip;

    @ApiModelProperty(value = "访问时间")
    @TableField("visit_time")
    private java.util.Date visitTime;

    @ApiModelProperty(value = "接口名称")
    @TableField("api_name")
    private String apiName;

    @ApiModelProperty(value = "接口类型")
    @TableField("api_type")
    private String apiType;
}
