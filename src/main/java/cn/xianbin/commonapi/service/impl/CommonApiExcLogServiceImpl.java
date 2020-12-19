package cn.xianbin.commonapi.service.impl;

import cn.xianbin.commonapi.mapper.CommonApiExcLogMapper;
import cn.xianbin.commonapi.entity.CommonApiExcLog;
import cn.xianbin.commonapi.service.ICommonApiExcLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommonApiExcLogServiceImpl extends ServiceImpl<CommonApiExcLogMapper, CommonApiExcLog> implements ICommonApiExcLogService {
    @Autowired
    private CommonApiExcLogMapper commonApiExcLogDao;

    @Override
    public List<CommonApiExcLog> selectByMsg(String apiCode, String msg) {
        return commonApiExcLogDao.selectByMsg(apiCode, msg);
    }
}
