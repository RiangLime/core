package cn.lime.core.mapper;

import cn.lime.core.module.entity.LoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author riang
* @description 针对表【Login_Log(用户登录日志表)】的数据库操作Mapper
* @createDate 2024-03-15 12:13:40
* @Entity cn.queertech.applyeasybackend.base.module.entity.LoginLog
*/
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

}




