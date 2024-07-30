package cn.lime.core.service.db.impl;


import cn.lime.core.constant.NickWordTypeEnum;
import cn.lime.core.mapper.NicknamerepoMapper;
import cn.lime.core.module.entity.Nicknamerepo;
import cn.lime.core.service.db.NicknamerepoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
* @author riang
* @description 针对表【NickNameRepo(昵称工具表)】的数据库操作Service实现
* @createDate 2024-03-15 12:13:40
*/
@Service
public class NicknamerepoServiceImpl extends ServiceImpl<NicknamerepoMapper, Nicknamerepo>
    implements NicknamerepoService {
    @Override
    public String getRandomNick() {
        List<Nicknamerepo> nouns = lambdaQuery().eq(Nicknamerepo::getType, NickWordTypeEnum.Noun.getValue()).list();
        List<Nicknamerepo> adjs = lambdaQuery().eq(Nicknamerepo::getType, NickWordTypeEnum.ADJ.getValue()).list();
        Random random = new Random();
        int nRandom = random.nextInt(nouns.size());
        int vRandom = random.nextInt(adjs.size());
        return adjs.get(vRandom).getContent() + nouns.get(nRandom).getContent();
    }
}




