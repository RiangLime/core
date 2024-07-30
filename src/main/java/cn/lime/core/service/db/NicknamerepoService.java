package cn.lime.core.service.db;

import cn.lime.core.module.entity.Nicknamerepo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author riang
* @description 针对表【NickNameRepo(昵称工具表)】的数据库操作Service
* @createDate 2024-03-15 12:13:40
*/
public interface NicknamerepoService extends IService<Nicknamerepo> {
    String getRandomNick();
}
