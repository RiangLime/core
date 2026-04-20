package cn.lime.core.service.db;

import cn.lime.core.module.entity.WxDeliverList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author riang
* @description 针对表【Wx_Deliver_List(微信运力表)】的数据库操作Service
* @createDate 2026-02-26 22:12:54
*/
public interface WxDeliverListService extends IService<WxDeliverList> {
    String getRelateDeliverId(String deliverCode);
}
