package cn.lime.core.service.db.impl;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.mapper.UserMapper;
import cn.lime.core.module.bean.OssUploadRsp;
import cn.lime.core.module.entity.User;
import cn.lime.core.module.entity.Userthirdauthorization;
import cn.lime.core.module.vo.LoginVo;
import cn.lime.core.service.db.UserService;
import cn.lime.core.service.db.UserthirdauthorizationService;
import cn.lime.core.service.login.UniLogService;
import cn.lime.core.service.oss.QiNiuOssService;
import cn.lime.core.service.wx.auth.WxMpOuterService;
import cn.lime.core.service.wx.bean.WxPhoneInfo;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import cn.lime.core.threadlocal.ReqThreadLocal;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiniu.common.QiniuException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

/**
* @author riang
* @description 针对表【User(用户表)】的数据库操作Service实现
* @createDate 2024-03-15 12:13:40
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Value("${apply-easy.wx.mp.app-key}")
    private String appId;
    @Value("${apply-easy.wx.mp.app-secret}")
    private String secretId;
    @Resource
    private SnowFlakeGenerator snowFlakeGenerator;
    @Resource
    private UserthirdauthorizationService thirdLoginService;
    @Resource
    private BasePhoneService phoneService;
    @Resource
    private WxMpOuterService wxMpOuterService;
    @Resource
    private UniLogService uniLogService;
    @Resource
    private QiNiuOssService ossService;

    @Override
    @Transactional
    public void register(String account, String pwd, String nickname, String phone, String code, String avatar, String email, Integer sex, Date birth,String birthplace) {
        // 验证手机验证码
        ThrowUtils.throwIf(!phoneService.checkCode(phone, code), ErrorCode.MOBILE_PHONE_MESSAGE_CODE_ERROR);
        // 检查账户是否存在
        ThrowUtils.throwIf(lambdaQuery().eq(User::getAccount, account).exists(), ErrorCode.ACCOUNT_EXIST);
        ThrowUtils.throwIf(lambdaQuery().eq(User::getPhone, phone).exists(), ErrorCode.PHONE_EXIST);
        // 进行PeerReview的注册
        User user = new User(snowFlakeGenerator.nextId(), account, pwd, nickname, avatar, phone, email, sex, birth, birthplace);
        ThrowUtils.throwIf(!save(user), ErrorCode.INSERT_ERROR, "注册用户信息失败");
    }

    @Override
    public void updatePhone(String oldPhone, String newPhone, String code) {
        // 验证新手机手机号码
        ThrowUtils.throwIf(!phoneService.checkCode(newPhone, code), ErrorCode.MOBILE_PHONE_MESSAGE_CODE_ERROR);
        User user = getById(ReqThreadLocal.getInfo().getUserId());
        ThrowUtils.throwIf(user.getPhone().equals(oldPhone), ErrorCode.PARAMS_ERROR, "旧手机号不正确");

        ThrowUtils.throwIf(lambdaQuery().eq(User::getPhone, newPhone).exists(), ErrorCode.PHONE_EXIST);
        // 更新手机号
        ThrowUtils.throwIf(lambdaUpdate().eq(User::getUserId, ReqThreadLocal.getInfo().getUserId())
                .set(User::getPhone, newPhone).update(), ErrorCode.UPDATE_ERROR, "更新用户手机号失败");
    }

    @Override
    public void updateUserAvatar(MultipartFile file, Long userId) {
        User user = getById(userId);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(user),ErrorCode.NOT_FOUND_ERROR);
        if (StringUtils.isNotEmpty(user.getAvatar())){
            String[] strArr = StringUtils.split(user.getAvatar(),"/");
            String fileName = strArr[strArr.length-1];
            try {
                ossService.deleteRemoteByFilename(fileName,userId);
            }catch (QiniuException e){
                log.error("删除用户头像异常" + user.getAvatar(),e);
            }
        }
        try {
            OssUploadRsp rsp = ossService.uploadFile(file.getBytes(),file.getName(),userId);
            ThrowUtils.throwIf(!lambdaUpdate().eq(User::getUserId,userId).set(User::getAvatar,rsp.getFileUrl()).update()
                    ,ErrorCode.UPDATE_ERROR,"更新用户头像至数据库异常");
        }catch (IOException e){
            throw new BusinessException(ErrorCode.IO_ERROR,"获取字节流异常");
        }
    }

    @Override
    @Transactional
    public void updateCommonInfo(String account, String nick, String avatar, String email, Integer sex, Date birth, String birthplace) {
        Long userId = ReqThreadLocal.getInfo().getUserId();
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getUserId,userId);
        if (StringUtils.isNotEmpty(account)){
            wrapper.set(User::getAccount,account);
            ThrowUtils.throwIf(lambdaQuery().eq(User::getAccount,account).exists(),ErrorCode.ACCOUNT_EXIST);
        }
        if (StringUtils.isNotEmpty(nick)){
            wrapper.set(User::getName,nick);
        }
        if (StringUtils.isNotEmpty(avatar)){
            wrapper.set(User::getAvatar,avatar);
        }
        if (StringUtils.isNotEmpty(email)){
            wrapper.set(User::getEmail,email);
        }
        if (ObjectUtils.isNotEmpty(sex)){
            wrapper.set(User::getSex,sex);
        }
        if (ObjectUtils.isNotEmpty(birth)){
            wrapper.set(User::getBirthday,birth);
        }
        if (StringUtils.isNotEmpty(birthplace)){
            wrapper.set(User::getBirthplace,birthplace);
        }
        ThrowUtils.throwIf(!update(wrapper),ErrorCode.UPDATE_ERROR,"更新用户信息失败");
    }

    @Override
    public void updatePwdInfo(String oldPwd, String newPwd) {
        // 获取用户详情
        User user = getById(ReqThreadLocal.getInfo().getUserId());
        // 用户密码错误
        ThrowUtils.throwIf(!user.getPassword().equals(oldPwd), ErrorCode.PWD_ERROR);
        // 更新密码
        ThrowUtils.throwIf(!lambdaUpdate().eq(User::getUserId, ReqThreadLocal.getInfo().getUserId())
                .set(User::getPassword, newPwd).update(), ErrorCode.UPDATE_ERROR, "更新用户密码失败");
    }

    @Override
    public void updatePwdInfo(String phone, String code, String newPwd) {
        // 核对验证码
        ThrowUtils.throwIf(!phoneService.checkCode(phone, code), ErrorCode.MOBILE_PHONE_MESSAGE_CODE_ERROR);
        // 获取用户详情
        User personnel = getById(ReqThreadLocal.getInfo().getUserId());
        // 验证手机号是否一致
        ThrowUtils.throwIf(!personnel.getPhone().equals(phone), ErrorCode.PHONE_ERROR);
        // 更新密码
        ThrowUtils.throwIf(!lambdaUpdate().eq(User::getUserId, ReqThreadLocal.getInfo().getUserId())
                .set(User::getPassword, newPwd).update(), ErrorCode.UPDATE_ERROR, "更新用户密码失败");
    }


    @Override
    @Transactional
    public void updateUserStatus(Long userId, Integer status) {
        ThrowUtils.throwIf(!lambdaQuery().eq(User::getUserId,userId).exists(),ErrorCode.NOT_FOUND_ERROR,"用户不存在");
        ThrowUtils.throwIf(!lambdaUpdate().eq(User::getUserId,userId).set(User::getStatus,status).update()
                ,ErrorCode.UPDATE_ERROR,"更新用户状态失败");
    }

    @Override
    @Transactional
    public LoginVo bindPhoneByPhone(String phone, String code) {
        // 验证新手机手机号码
        ThrowUtils.throwIf(!phoneService.checkCode(phone, code), ErrorCode.MOBILE_PHONE_MESSAGE_CODE_ERROR);
        return doBindPhone(phone);
    }

    @Override
    @Transactional
    public LoginVo bindPhoneByPhoneCode(String phoneCode) {
        // 获取手机号
        WxPhoneInfo wxPhoneInfo = wxMpOuterService.getWxPhoneInfo(appId, secretId, phoneCode);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(wxPhoneInfo), ErrorCode.WX_PHONE_INTERFACE_ERROR);
        return doBindPhone(wxPhoneInfo.getPurePhoneNumber());
    }

    @Transactional
    public LoginVo doBindPhone(String phone) {
        // 调用接口查询该手机号是否已被绑定
        // 如果此处personnel不为空,代表已经有手机号注册过
        User personnel = lambdaQuery().eq(User::getPhone, phone).one();
        // 之前没注册过
        if (ObjectUtils.isEmpty(personnel)) {
            // 新账号绑定手机号
            ThrowUtils.throwIf(!lambdaUpdate().eq(User::getUserId, ReqThreadLocal.getInfo().getUserId())
                    .set(User::getPhone, phone).update(), ErrorCode.UPDATE_ERROR, "用户绑定手机号异常");
            return null;
        }
        // 之前有注册过
        else {
            // 判断是否已经绑定
            ThrowUtils.throwIf(thirdLoginService.lambdaQuery()
                            .eq(Userthirdauthorization::getPersonnelId, personnel.getUserId()).exists(),
                    ErrorCode.PHONE_EXIST, "该手机号已经被绑定且账号已绑定其他账号号");
            // 删除业务用户表的新增的一键注册用户
            ThrowUtils.throwIf(!removeById(ReqThreadLocal.getInfo().getUserId()), ErrorCode.DELETE_ERROR, "删除微信新注册用户失败");
            // 更新第三方登录关联表
            thirdLoginService.lambdaUpdate()
                    .eq(Userthirdauthorization::getPersonnelId, ReqThreadLocal.getInfo().getUserId())
                    .set(Userthirdauthorization::getPersonnelId, personnel.getUserId()).update();

            return uniLogService.generateToken(personnel,ReqThreadLocal.getInfo().getPlatform(), ReqThreadLocal.getInfo().getIp());
        }
    }
}




