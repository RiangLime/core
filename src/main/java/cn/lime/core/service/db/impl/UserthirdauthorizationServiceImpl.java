package cn.lime.core.service.db.impl;

import cn.lime.core.constant.ThirdAuthorizationType;
import cn.lime.core.mapper.UserthirdauthorizationMapper;
import cn.lime.core.module.entity.Userthirdauthorization;
import cn.lime.core.service.db.UserthirdauthorizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
* @author riang
* @description 针对表【UserThirdAuthorization(用户第三方登录授权表)】的数据库操作Service实现
* @createDate 2024-03-15 12:13:40
*/
@Service
public class UserthirdauthorizationServiceImpl extends ServiceImpl<UserthirdauthorizationMapper, Userthirdauthorization>
    implements UserthirdauthorizationService {
    @Override
    public String getUserWxOpenId(Long userId) {
        Optional<Userthirdauthorization> userthirdauthorization = lambdaQuery().eq(Userthirdauthorization::getPersonnelId, userId)
                .eq(Userthirdauthorization::getThirdType, ThirdAuthorizationType.Wechat.getVal()).oneOpt();
        return userthirdauthorization.map(Userthirdauthorization::getThirdSecondTag).orElse(null);
    }
}




