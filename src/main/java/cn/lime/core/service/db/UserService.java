package cn.lime.core.service.db;

import cn.lime.core.common.PageResult;
import cn.lime.core.module.entity.User;
import cn.lime.core.module.vo.LoginVo;
import cn.lime.core.module.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @author riang
 * @description 针对表【User(用户表)】的数据库操作Service
 * @createDate 2024-03-15 12:13:40
 */
public interface UserService extends IService<User> {
    // user interfaces
    void register(String account, String pwd, String nickname, String phone, String code, String avatar, String email, Integer sex, Date birth, String birthplace);

    LoginVo bindPhoneByPhoneCode(String phoneCode);

    LoginVo bindPhoneByPhone(String phone, String code);

    void updatePhone(String oldPhone, String newPhone, String code);

    void updateCommonInfo(String account, String nick, String avatar, String email, Integer sex, Date birth, String birthplace);

    void updatePwdInfo(String oldPwd, String newPwd);

    void updatePwdInfo(String phone, String code, String newPwd);

    void updateUserStatus(Long userId, Integer status);


    // admin interfaces
    PageResult<UserVo> page(String queryField, Long registerStart, Long registerEnd, Integer userState, Integer userVipLevel,
                            Integer current, Integer pageSize,String sortOrder,String sortField);

    PageResult<UserVo> page(String queryField, Long registerStart, Long registerEnd, Integer userState, Integer userVipLevel,
                            Integer spendMoneyStart, Integer spendMoneyEnd,
                            Integer current, Integer pageSize,String sortOrder,String sortField);

    List<User> list(Integer current, Integer pageSize,String sortOrder,String sortField);

    UserVo detail(Long userId);
    boolean freeze(Long userId);
    boolean unfreeze(Long userId);
}
