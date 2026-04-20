package cn.lime.core.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.config.CoreParams;
import cn.lime.core.service.wx.auth.WxMpOuterService;
import cn.lime.core.utils.ExpressCompanyRecognizer;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.core.module.entity.WxDeliverList;
import cn.lime.core.service.db.WxDeliverListService;
import cn.lime.core.mapper.WxDeliverListMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
* @author riang
* @description 针对表【Wx_Deliver_List(微信运力表)】的数据库操作Service实现
* @createDate 2026-02-26 22:12:54
*/
@Service
public class WxDeliverListServiceImpl extends ServiceImpl<WxDeliverListMapper, WxDeliverList>
    implements WxDeliverListService{

    @Resource
    private CoreParams coreParams;
    @Resource
    private WxMpOuterService wxMpOuterService;


    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE)
    @Transactional
    public void startUpDelay(){
        autoUpdate();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void startDaily(){
        autoUpdate();
    }

    @Transactional
    public void autoUpdate(){
        if (!coreParams.getAutoUpdateWxDeliverInfo())
            return;
        List<WxDeliverList> list = wxMpOuterService.getWxDeliverList(coreParams.getWxMpAppId(),coreParams.getWxMpSecretId());
        long count = lambdaQuery().count();
        if (count>0) {
            ThrowUtils.throwIf(!lambdaUpdate().remove(), ErrorCode.DELETE_ERROR, "删除微信运力信息失败");
        }
        ThrowUtils.throwIf(!saveBatch(list), ErrorCode.DELETE_ERROR,"批量存储微信运力信息失败");

    }

    @Override
    public String getRelateDeliverId(String deliverCode) {
        // 识别快递公司（返回所有匹配结果）
        String deliverName = ExpressCompanyRecognizer.getMostLikelyCompany(deliverCode);
        ThrowUtils.throwIf(StringUtils.isEmpty(deliverName),ErrorCode.PARAMS_ERROR,"无法根据快递单号找到合适的快递公司");
        Optional<WxDeliverList> wxDeliverList = lambdaQuery().eq(WxDeliverList::getDeliveryName, deliverName).oneOpt();
        ThrowUtils.throwIf(wxDeliverList.isEmpty(),ErrorCode.PARAMS_ERROR,
                "分析的快递公司为"+deliverName+",无法在微信运力表中找到相关快递公司ID");
        return wxDeliverList.get().getDeliveryId();
    }
}




