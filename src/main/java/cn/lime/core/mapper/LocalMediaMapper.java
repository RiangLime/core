package cn.lime.core.mapper;

import cn.lime.core.module.entity.LocalMedia;
import cn.lime.core.module.vo.LocalMediaVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
* @author riang
* @description 针对表【Local_Media(本地多媒体文件表)】的数据库操作Mapper
* @createDate 2024-09-03 11:51:27
* @Entity cn.lime.core.module.entity.LocalMedia
*/
public interface LocalMediaMapper extends BaseMapper<LocalMedia> {
    Page<LocalMediaVo> pageMedia(Long tagId,Page<?> page);
}




