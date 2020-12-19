
package cn.xianbin.commonapi.handler;

import cn.xianbin.commonapi.exception.SourceException;
import cn.xianbin.commonapi.rest.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = SourceException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private Resp<?> commonSourceExceptionHandler(HttpServletRequest request, Exception e) {
        log.error(e.getMessage(), e);
        return Resp.failed(e.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private Resp<?> commonExceptionHandler(HttpServletRequest request, Exception e) {
        log.error(e.getMessage(), e);
        return Resp.failed(e.getMessage());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private Resp<?> httpMessageNotReadableExceptionHandler(HttpServletRequest request, HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        return Resp.failed("参数格式解析异常");
    }

}
