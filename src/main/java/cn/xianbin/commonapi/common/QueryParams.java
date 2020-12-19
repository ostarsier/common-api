package cn.xianbin.commonapi.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class QueryParams<T> {

    private static ThreadLocal<Integer> threadLocalPageNo = new ThreadLocal<>();
    private static ThreadLocal<Integer> threadLocalPageSize = new ThreadLocal<>();

    public static void setQueryParams(Integer pageNo, Integer pageSize) {
        threadLocalPageNo.set(pageNo);
        threadLocalPageSize.set(pageSize);
    }

    /**
     * 分页
     */
    public static Page getPage() {
        Integer pageNo = threadLocalPageNo.get();
        Integer pageSize = threadLocalPageSize.get();
        if (pageNo == null) {
            pageNo = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        return new Page(pageNo, pageSize);
    }

    public static void clear() {
        threadLocalPageNo.remove();
        threadLocalPageSize.remove();
    }
}
