package cn.lime.core.service.db;

import cn.lime.core.module.entity.LocalMediaTag;
import cn.lime.core.module.vo.LocalMediaTagVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author riang
* @description 针对表【Local_Media_Tag(本地多媒体文件标签表)】的数据库操作Service
* @createDate 2024-09-03 11:51:27
*/
public interface LocalMediaTagService extends IService<LocalMediaTag> {
    void addTag(String name);
    List<LocalMediaTagVo> listTags();
    void deleteTag(Long tagId);
}
