package cn.xianbin.commonapi.mapper;

import cn.xianbin.commonapi.entity.CommonApiExcLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommonApiExcLogMapper extends BaseMapper<CommonApiExcLog> {
    List<CommonApiExcLog> selectByMsg(String apiCode, String msg);
}
