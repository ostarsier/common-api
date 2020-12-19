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
public class CommonApiConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "内码")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "接口编码")
    @TableField("api_code")
    private String apiCode;

    @ApiModelProperty(value = "接口名称")
    @TableField("api_name")
    private String apiName;

    @ApiModelProperty(value = "说明")
    @TableField("note")
    private String note;

    @ApiModelProperty(value = "数据源")
    @TableField("ds_id")
    private String dsId;

    @ApiModelProperty(value = "sql")
    @TableField("sql_txt")
    private String sqlTxt;

    @ApiModelProperty(value = "条件")
    @TableField("where_txt")
    private String whereTxt;

    @ApiModelProperty(value = "查询结果字段对应中文备注")
    @TableField("filed_note")
    private String filedNote;

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
