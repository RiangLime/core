package cn.lime.core.service.wx.payment;


import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.constant.PaymentTypeEnum;
import cn.lime.core.loader.ApplicationContextHolder;

/**
 * @ClassName: WxPayFactory
 * @Description: 微信支付同意入口
 * @Author: Lime
 * @Date: 2023/10/19 11:27
 */
public class WxPayFactory {

    public static WxPayService get(int which){
        if (which== PaymentTypeEnum.WX_PAY_BASE.getVal()) {
            return (WxPayService) ApplicationContextHolder.getContext().getBean("baseWxPayServiceImpl");
        } else if (which==PaymentTypeEnum.CODE.getVal()) {
            return ApplicationContextHolder.getContext().getBean(NativePayServiceImpl.class);
        } else if (which== PaymentTypeEnum.JS_API.getVal()) {
            return ApplicationContextHolder.getContext().getBean(JsApiPayServiceImpl.class);
        } else if(which == PaymentTypeEnum.H5.getVal()){
            return ApplicationContextHolder.getContext().getBean(H5PayServiceImpl.class);
        }else {
            throw new BusinessException(ErrorCode.PAY_METHOD_UNSUPPORTED);
        }
    }

}
