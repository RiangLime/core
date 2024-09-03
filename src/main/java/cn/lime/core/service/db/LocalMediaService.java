package cn.lime.core.service.db;

import cn.lime.core.common.PageResult;
import cn.lime.core.module.entity.LocalMedia;
import cn.lime.core.module.vo.LocalMediaVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author riang
* @description 针对表【Local_Media(本地多媒体文件表)】的数据库操作Service
* @createDate 2024-09-03 11:51:27
*/
public interface LocalMediaService extends IService<LocalMedia> {
    void addUrl(String url,Long tagId);
    void deleteUrl(Long urlId);
    PageResult<LocalMediaVo> pageUrl(Long tagId,Integer current,Integer pageSize);
}
