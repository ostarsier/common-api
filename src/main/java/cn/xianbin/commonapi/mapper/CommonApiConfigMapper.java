package cn.xianbin.commonapi.mapper;

import cn.xianbin.commonapi.entity.CommonApiConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonApiConfigMapper extends BaseMapper<CommonApiConfig> {
    CommonApiConfig getConfigByApiCode(String apiCode);
}
