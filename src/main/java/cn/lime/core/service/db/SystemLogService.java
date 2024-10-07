package cn.lime.core.service.db;

import cn.lime.core.module.entity.SystemLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author riang
* @description 针对表【System_Log(系统调用日志)】的数据库操作Service
* @createDate 2024-09-19 17:08:53
*/
public interface SystemLogService extends IService<SystemLog> {
    void add(String interfaceName,String token);
}
