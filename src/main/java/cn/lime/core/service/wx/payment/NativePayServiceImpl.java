package cn.lime.core.service.wx.payment;

import cn.lime.core.module.vo.OrderPayVo;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest;
import org.springframework.stereotype.Service;

/**
 * @ClassName: NativePayServiceImpl
 * @Description: native支付方式具体实现类 扫码
 * @Author: Lime
 * @Date: 2023/9/28 11:51
 */
@Service
public class NativePayServiceImpl extends BaseWxPayServiceImpl {

    @Override
    public OrderPayVo prepay(Long orderId, Integer price, String notifyUrl, String openId) {
        Amount amount = new Amount();
        amount.setTotal(price);
        PrepayRequest request = new PrepayRequest();
        request.setAmount(amount);
        request.setMchid(coreParams.getWxPayMerchantId());
        request.setAppid(coreParams.getWxPayApId());
        request.setNotifyUrl(coreParams.getWxPayNotifyUrlPrefix() + notifyUrl);
        request.setOutTradeNo(String.valueOf(orderId));
        request.setDescription("PeerReview Order " + orderId);
        // 预下单
        PrepayResponse response = nativeService.prepay(request);
        cacheOrder(orderId);
        OrderPayVo res = new OrderPayVo();
        res.setUrlCode(response.getCodeUrl());
        return res;
    }

    @Override
    public Transaction queryOrderById(Long orderId) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(String.valueOf(orderId));
        request.setMchid(String.valueOf(coreParams.getWxPayMerchantId()));
        return nativeService.queryOrderByOutTradeNo(request);
    }

}