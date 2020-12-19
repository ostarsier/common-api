package cn.xianbin.commonapi.interceptor;

import cn.xianbin.commonapi.common.QueryParams;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 常用查询参数拦截,放到ThreadLocal,方便Service层获取
 *
 * @see QueryParams
 */
public class QueryParamsInterceptor implements HandlerInterceptor {

    /**
     * 第几页
     */
    private static final String PAGE_NO = "pageNo";
    /**
     * 每页多少条
     */
    private static final String PAGE_SIZE = "pageSize";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer pageNo = toInteger(request, PAGE_NO);
        Integer pageSize = toInteger(request, PAGE_SIZE);
        if (pageNo != null && pageSize != null) {
            QueryParams.setQueryParams(pageNo, pageSize);
        }
        return true;
    }

    private Integer toInteger(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return StringUtils.isEmpty(value) ? null : Integer.valueOf(value);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        QueryParams.clear();
    }

}
