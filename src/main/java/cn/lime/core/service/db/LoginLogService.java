package cn.lime.core.service.db;

import cn.lime.core.module.entity.LoginLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author riang
* @description 针对表【Login_Log(用户登录日志表)】的数据库操作Service
* @createDate 2024-03-15 12:13:40
*/
public interface LoginLogService extends IService<LoginLog> {
    void appendLog(Long userId,String ip,Integer platform);
}
