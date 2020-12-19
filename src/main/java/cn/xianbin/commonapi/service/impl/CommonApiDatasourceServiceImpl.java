package cn.xianbin.commonapi.service.impl;

import cn.xianbin.commonapi.common.QueryParams;
import cn.xianbin.commonapi.dto.DataSourceAddDto;
import cn.xianbin.commonapi.dto.DataSourceDto;
import cn.xianbin.commonapi.entity.CommonApiDatasource;
import cn.xianbin.commonapi.exception.ServerException;
import cn.xianbin.commonapi.mapper.CommonApiDatasourceMapper;
import cn.xianbin.commonapi.service.ICommonApiDatasourceService;
import cn.xianbin.commonapi.util.DataSourceUtil;
import cn.xianbin.commonapi.vo.JdbcSourceInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Service
public class CommonApiDatasourceServiceImpl extends ServiceImpl<CommonApiDatasourceMapper, CommonApiDatasource> implements ICommonApiDatasourceService {
    @Autowired
    private CommonApiDatasourceMapper commonApiDatasourceDao;

    @Override
    public CommonApiDatasource selectById(Serializable id) {
        return commonApiDatasourceDao.selectById(id);
    }

    @Override
    public List<DataSourceDto> selectByName(String name) {
        return commonApiDatasourceDao.selectByName(name);
    }

    @Override
    public int insert(DataSourceAddDto dto) {
        List<DataSourceDto> sources = commonApiDatasourceDao.selectByName(dto.getName());
        if (!sources.isEmpty()) {
            throw new ServerException("当前数据源名称已存在");
        }
        CommonApiDatasource datasource = new CommonApiDatasource();
        BeanUtils.copyProperties(dto, datasource);
        datasource.setCreateTime(new Date());
        //验证数据数据源是否正常
        JdbcSourceInfo jdbcSourceInfo = DataSourceUtil.getJdbcSourceInfo(datasource);
        DataSourceUtil.getDataSource(jdbcSourceInfo);
        return commonApiDatasourceDao.insert(datasource);
    }

    @Override
    public int update(DataSourceDto dto) {
        CommonApiDatasource datasource = commonApiDatasourceDao.selectById(dto.getId());
        if (null == datasource) {
            throw new ServerException("当前数据源不存在");
        }
        //释放原数据源
        JdbcSourceInfo jdbcSourceInfo = DataSourceUtil.getJdbcSourceInfo(datasource);
        DataSourceUtil.removeDatasource(jdbcSourceInfo);
        BeanUtils.copyProperties(dto, datasource);
        datasource.setUpdateTime(new Date());
        //验证新数据数据源是否正常
        jdbcSourceInfo = DataSourceUtil.getJdbcSourceInfo(datasource);
        DataSourceUtil.getDataSource(jdbcSourceInfo);
        return commonApiDatasourceDao.updateById(datasource);
    }

    @Override
    public int delete(int id) {
        CommonApiDatasource datasource = commonApiDatasourceDao.selectById(id);
        if (null == datasource) {
            throw new ServerException("当前数据源不存在");
        }
        //释放原数据源
        JdbcSourceInfo jdbcSourceInfo = DataSourceUtil.getJdbcSourceInfo(datasource);
        DataSourceUtil.removeDatasource(jdbcSourceInfo);
        return commonApiDatasourceDao.deleteById(id);
    }


    public IPage<CommonApiDatasource> pageList(String keyword) {
        Page page = QueryParams.getPage();
        LambdaQueryWrapper<CommonApiDatasource> wrapper = new QueryWrapper().lambda();
        if (StringUtils.isNotBlank(keyword)) {
            wrapper.like(CommonApiDatasource::getName, keyword);
        }
        wrapper.orderByDesc(CommonApiDatasource::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

}
