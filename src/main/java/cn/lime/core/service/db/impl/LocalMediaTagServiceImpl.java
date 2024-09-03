package cn.lime.core.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.module.entity.LocalMedia;
import cn.lime.core.module.vo.LocalMediaTagVo;
import cn.lime.core.service.db.LocalMediaService;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.core.module.entity.LocalMediaTag;
import cn.lime.core.service.db.LocalMediaTagService;
import cn.lime.core.mapper.LocalMediaTagMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author riang
* @description 针对表【Local_Media_Tag(本地多媒体文件标签表)】的数据库操作Service实现
* @createDate 2024-09-03 11:51:27
*/
@Service
public class LocalMediaTagServiceImpl extends ServiceImpl<LocalMediaTagMapper, LocalMediaTag>
    implements LocalMediaTagService{
    @Resource
    private SnowFlakeGenerator ids;
    @Resource
    private LocalMediaService mediaService;

    @Override
    public void addTag(String name) {
        LocalMediaTag tag = new LocalMediaTag();
        tag.setId(ids.nextId());
        tag.setTagName(name);
        ThrowUtils.throwIf(!save(tag), ErrorCode.INSERT_ERROR);
    }

    @Override
    public List<LocalMediaTagVo> listTags() {
        return list().stream().map(LocalMediaTagVo::fromBean).toList();
    }

    @Override
    public void deleteTag(Long tagId) {
        ThrowUtils.throwIf(tagId==1,ErrorCode.PARAMS_ERROR,"'未分组'标签不可删除");
        ThrowUtils.throwIf(!mediaService.lambdaUpdate().eq(LocalMedia::getUrlTagId,tagId).set(LocalMedia::getUrlTagId,1)
                .update(),ErrorCode.UPDATE_ERROR,"将"+tagId+"标签下的URL变更为默认分组异常");
        ThrowUtils.throwIf(!lambdaUpdate().eq(LocalMediaTag::getId,tagId).remove(),ErrorCode.DELETE_ERROR);
    }
}




