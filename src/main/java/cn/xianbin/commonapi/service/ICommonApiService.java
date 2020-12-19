package cn.xianbin.commonapi.service;

import cn.xianbin.commonapi.dto.Page;
import cn.xianbin.commonapi.dto.WhereDto;
import cn.xianbin.commonapi.entity.CommonApiConfig;
import cn.xianbin.commonapi.entity.CommonApiDatasource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface ICommonApiService {
    void getData(String params, CommonApiConfig commonApiConfig, CommonApiDatasource commonApiDatasource, Page<?> page, HttpServletRequest request);

    List<WhereDto> getParams(String whereTxt);
}
