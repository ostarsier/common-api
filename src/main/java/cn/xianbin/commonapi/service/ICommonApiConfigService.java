package cn.xianbin.commonapi.service;

import cn.xianbin.commonapi.dto.ApiConfigAddDto;
import cn.xianbin.commonapi.dto.ApiConfigDto;
import cn.xianbin.commonapi.entity.CommonApiConfig;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ICommonApiConfigService extends IService<CommonApiConfig> {
    CommonApiConfig getConfigByApiCode(String apiCode);
    ApiConfigDto selectByApiCode(String apiCode);
    int insert(ApiConfigAddDto srcDto);
    int update(ApiConfigDto dto);
    int delete(int id);

    IPage<CommonApiConfig> pageList(String keyword);
}
