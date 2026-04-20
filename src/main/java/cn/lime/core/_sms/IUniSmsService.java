package cn.lime.core._sms;


import cn.lime.core.module.dto.unidto.UniEasyLoginDto;
import cn.lime.core.module.vo.LoginVo;
import cn.lime.core.module.vo.TokenCheckVo;

/**
 * @ClassName: IUniSmsService
 * @Description: 统一SMS接口
 * @Author: Lime
 * @Date: 2023/12/11 13:41
 */
public interface IUniSmsService {

    void sendMessage(String mobilePhone,String type);
    boolean checkCode(String mobilePhone, String code);

}
