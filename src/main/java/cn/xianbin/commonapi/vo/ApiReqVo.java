package cn.xianbin.commonapi.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * api请求体
 */
@Data
public class ApiReqVo {

    @ApiModelProperty(value = "接口编码")
    private String apiCode;

    @ApiModelProperty(value = "接口参数，json的字符串")
    private JSONObject params;

}
