package cn.xianbin.commonapi.mapper;

import cn.xianbin.commonapi.dto.DataSourceDto;
import cn.xianbin.commonapi.entity.CommonApiDatasource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonApiDatasourceMapper extends BaseMapper<CommonApiDatasource> {
    List<DataSourceDto> selectByName(String name);
}
