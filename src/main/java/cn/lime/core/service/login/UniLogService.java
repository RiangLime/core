package cn.lime.core.service.login;


import cn.lime.core.module.dto.unidto.UniEasyLoginDto;
import cn.lime.core.module.entity.User;
import cn.lime.core.module.vo.LoginVo;
import cn.lime.core.module.vo.TokenCheckVo;

/**
 * @ClassName: UniLogService
 * @Description: 统一登录接口
 * @Author: Lime
 * @Date: 2023/12/11 13:41
 */
public interface UniLogService {

    // 外部可访问
    LoginVo easyLogin(UniEasyLoginDto dto);

    // jwt无登出 前端删除即可
    @Deprecated
    void logout();

    // jwt
    String refreshToken(String refreshToken);

    /**
     * 合并token校验  统一使用jwt
     * @param accessToken token
     * @return 校验token vo
     */
    TokenCheckVo checkJwtAccessToken(String accessToken);


    LoginVo generateToken(User user, Integer platform, String ip);


}
