package cn.lime.core.service.db.impl;

import cn.lime.core.mapper.LoginLogMapper;
import cn.lime.core.module.entity.LoginLog;
import cn.lime.core.service.db.LoginLogService;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author riang
* @description 针对表【Login_Log(用户登录日志表)】的数据库操作Service实现
* @createDate 2024-03-15 12:13:40
*/
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog>
    implements LoginLogService {
    @Resource
    private SnowFlakeGenerator ids;
    @Override
    public void appendLog(Long userId, String ip, Integer platform) {
        LoginLog log = new LoginLog();
        log.setId(ids.nextId());
        log.setLoginIp(ip);
        log.setUserId(userId);
        log.setLoginPlatform(platform);
        log.setLoginTime(System.currentTimeMillis());
        save(log);
    }
}




