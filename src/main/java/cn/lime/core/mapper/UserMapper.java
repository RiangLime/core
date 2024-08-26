package cn.lime.core.mapper;

import cn.lime.core.module.entity.User;
import cn.lime.core.module.vo.UserVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
* @author riang
* @description 针对表【User(用户表)】的数据库操作Mapper
* @createDate 2024-03-15 12:13:40
* @Entity cn.queertech.applyeasybackend.base.module.entity.User
*/
public interface UserMapper extends BaseMapper<User> {
    Page<UserVo> pageWithoutMall(String queryField, Long registerStart, Long registerEnd, Integer userState, Integer userVipLevel,Page<?> page);
    UserVo detail(Long userId);
}




