package cn.lime.core._login.strategy;


import cn.lime.core.module.dto.unidto.UniEasyLoginDto;

/**
 * @ClassName: UniLogService
 * @Description: 统一登录接口
 * @Author: Lime
 * @Date: 2023/12/11 13:41
 */
public interface LoginStrategy {

    // 外部可访问
    LoginStrategyRes loginCheck(UniEasyLoginDto dto);

    String getType();

}
