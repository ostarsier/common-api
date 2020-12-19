package cn.xianbin.commonapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class CommonApiDatasource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内码")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "备注")
    @TableField("note")
    private String note;

    @ApiModelProperty(value = "数据库链接")
    @TableField("url")
    private String url;

    @ApiModelProperty(value = "数据库类型")
    @TableField("type")
    private String type;

    @ApiModelProperty(value = "数据库用户名")
    @TableField("username")
    private String username;

    @ApiModelProperty(value = "数据库密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private java.util.Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private java.util.Date updateTime;

    @ApiModelProperty(value = "创建人")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty(value = "更新人")
    @TableField("update_by")
    private String updateBy;
}
