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
public class CommonApiExcLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(value = "接口编码")
    @TableField("api_code")
    private String apiCode;

    @ApiModelProperty(value = "执行sql")
    @TableField("exc_sql")
    private String excSql;

    @ApiModelProperty(value = "执行时间")
    @TableField("exc_time")
    private java.util.Date excTime;

    @ApiModelProperty(value = "执行次数")
    @TableField("exc_count")
    private Integer excCount;

    @ApiModelProperty(value = "执行信息")
    @TableField("exc_msg")
    private String excMsg;

    @ApiModelProperty(value = "执行参数")
    @TableField("exc_param")
    private String excParam;

    @ApiModelProperty(value = "备用字段1")
    @TableField("filed1")
    private String filed1;

    @ApiModelProperty(value = "备用字段2")
    @TableField("filed2")
    private String filed2;

    @ApiModelProperty(value = "备用字段3")
    @TableField("filed3")
    private String filed3;

    @ApiModelProperty(value = "备用字段4")
    @TableField("filed4")
    private String filed4;

    @ApiModelProperty(value = "备用字段5")
    @TableField("filed5")
    private String filed5;
}
