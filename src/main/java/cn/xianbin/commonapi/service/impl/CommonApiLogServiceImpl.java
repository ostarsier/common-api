package cn.xianbin.commonapi.service.impl;

import cn.xianbin.commonapi.mapper.CommonApiLogMapper;
import cn.xianbin.commonapi.entity.CommonApiLog;
import cn.xianbin.commonapi.service.ICommonApiLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CommonApiLogServiceImpl extends ServiceImpl<CommonApiLogMapper, CommonApiLog> implements ICommonApiLogService {

}
