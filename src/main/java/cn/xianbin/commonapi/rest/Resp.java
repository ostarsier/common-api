package cn.xianbin.commonapi.rest;

import cn.xianbin.commonapi.constant.HttpCodeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class Resp<T> {
    @ApiModelProperty(value = "状态码(200-成功, 0-失败)", example = "0", position = 1)
    @Getter
    @Setter
    private final int code;
    @ApiModelProperty(value = "提示消息", example = "提示消息", position = 1)
    @Getter
    @Setter
    private final String msg;
    @ApiModelProperty(value = "数据集", position = 1)
    @Getter
    @Setter
    private T data;

    private Resp(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Resp(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Resp<T> success(T data) {
        return new Resp<>(HttpCodeEnum.SUCCESS_CODE.getCode(), HttpCodeEnum.SUCCESS_CODE.getMsg(), data);
    }

    public static <T> Resp<T> success() {
        return new Resp<>(HttpCodeEnum.SUCCESS_CODE.getCode(), HttpCodeEnum.SUCCESS_CODE.getMsg());
    }

    public static <T> Resp<T> failed(String msg) {
        return new Resp<>(HttpCodeEnum.FAIL_CODE.getCode(), msg);
    }

    public static <T> Resp<T> failed() {
        return new Resp<>(HttpCodeEnum.FAIL_CODE.getCode(), HttpCodeEnum.FAIL_CODE.getMsg());
    }

    public static <T> Resp<T> failed(HttpCodeEnum httpCodeEnum) {
        return new Resp<>(httpCodeEnum.getCode(), httpCodeEnum.getMsg());
    }

}
