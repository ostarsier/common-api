package cn.xianbin.commonapi.controller;

import com.alibaba.druid.util.StringUtils;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;


@Component
public abstract class BaseController {

    @Getter
    private HttpServletRequest request;

    @ModelAttribute
    protected void accessRecord(HttpServletRequest request) {
        this.request = request;
    }

    public boolean invalidString(String val) {
        return StringUtils.isEmpty(val);
    }

    public boolean invalidInteger(Integer val) {
        return null == val;
    }
}
