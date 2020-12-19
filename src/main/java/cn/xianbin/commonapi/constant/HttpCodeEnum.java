package cn.xianbin.commonapi.constant;

import lombok.Getter;

@Getter
public enum HttpCodeEnum {
    //请求成功返回信息
    SUCCESS_CODE(200, "请求成功"),
    FAIL_CODE(0, "请求失败"),

    /**
     * 参数验证 9000-9999
     */
    PARAMETERS_ERROY(99, "参数错误"),
    PARAM_NULL(9991, "参数错误,参数不能为空"),
    PARAM_VAILD_ERROR(9992, "参数验证异常"),

    /**
     * 系统状态 500-599
     */
    SYSTEM_ERROR(500, "服务器错误"),

    /**
     * 数据操作 400-500
     */
    DATA_NULL(405, "没有查询到相关资源"),
    LABEL_NULL(405, "不存在此标签"),

    /**
     * 业务相关 200-300
     */
    EMPTY_CHECK_PHONE_SUC(201, "检测的号码不是空号"),
    EMPTY_CHECK_PHONE_FAIL(202, "检测的号码是空号"),
    EMPTY_CHECK_PHONE_RISK(203, "检测的号码是风险号"),
    EMPTY_CHECK_PHONE_UNKNOWN(204, "检测的号码是沉默号"),
    EMPTY_CHECK_PHONE_ERRO(250, "号码检测出现异常");


    private int code;
    private String msg;

    HttpCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
