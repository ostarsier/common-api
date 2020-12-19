package cn.xianbin.commonapi.service;

import cn.xianbin.commonapi.dto.DataSourceAddDto;
import cn.xianbin.commonapi.dto.DataSourceDto;
import cn.xianbin.commonapi.entity.CommonApiDatasource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

public interface ICommonApiDatasourceService extends IService<CommonApiDatasource> {
    CommonApiDatasource selectById(Serializable id);
    List<DataSourceDto> selectByName(String name);
    int insert(DataSourceAddDto srcDto);
    int update(DataSourceDto dto);
    int delete(int id);

    IPage<CommonApiDatasource> pageList(String keyword);
}
