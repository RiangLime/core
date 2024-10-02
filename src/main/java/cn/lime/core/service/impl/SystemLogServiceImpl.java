package cn.lime.core.service.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.core.module.entity.SystemLog;
import cn.lime.core.service.SystemLogService;
import cn.lime.core.mapper.SystemLogMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
* @author riang
* @description 针对表【System_Log(系统调用日志)】的数据库操作Service实现
* @createDate 2024-09-19 17:08:53
*/
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper, SystemLog>
    implements SystemLogService{
    @Resource
    private SnowFlakeGenerator ids;
    @Override
    public void add(String interfaceName, String token) {
        SystemLog bean = new SystemLog();
        bean.setId(ids.nextId());
        bean.setInterfaceName(interfaceName);
        bean.setToken(token);
        ThrowUtils.throwIf(!save(bean), ErrorCode.INSERT_ERROR);
    }
}




