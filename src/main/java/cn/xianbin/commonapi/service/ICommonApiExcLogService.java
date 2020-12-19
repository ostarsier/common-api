package cn.xianbin.commonapi.service;

import cn.xianbin.commonapi.entity.CommonApiExcLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ICommonApiExcLogService extends IService<CommonApiExcLog> {
    List<CommonApiExcLog> selectByMsg(String apiCode,String msg);
}
