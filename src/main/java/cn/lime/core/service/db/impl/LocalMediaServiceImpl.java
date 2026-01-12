package cn.lime.core.service.db.impl;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.PageResult;
import cn.lime.core.common.PageUtils;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.module.vo.LocalMediaVo;
import cn.lime.core.service.filestore.FileStorageService;
import cn.lime.core.snowflake.SnowFlakeGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lime.core.module.entity.LocalMedia;
import cn.lime.core.service.db.LocalMediaService;
import cn.lime.core.mapper.LocalMediaMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author riang
 * @description 针对表【Local_Media(本地多媒体文件表)】的数据库操作Service实现
 * @createDate 2024-09-03 11:51:27
 */
@Service
public class LocalMediaServiceImpl extends ServiceImpl<LocalMediaMapper, LocalMedia>
        implements LocalMediaService {
    @Resource
    private SnowFlakeGenerator ids;
    @Resource
    private FileStorageService fileStorageService;

    @Override
    public void addUrl(String url, Long tagId) {
        LocalMedia media = new LocalMedia();
        media.setId(ids.nextId());
        media.setUrl(url);
        media.setUrlTagId(tagId);
        ThrowUtils.throwIf(!save(media), ErrorCode.INSERT_ERROR);
    }

    @Override
    @Transactional
    public void deleteUrl(Long urlId) {
        LocalMedia media = getById(urlId);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(media),ErrorCode.NOT_FOUND_ERROR);
        String url = media.getUrl();
        ThrowUtils.throwIf(!lambdaUpdate().eq(LocalMedia::getId, urlId).remove(), ErrorCode.DELETE_ERROR);
        if (!lambdaQuery().eq(LocalMedia::getUrl,url).exists()){
            fileStorageService.deleteFile(url);
        }
    }

    @Override
    public PageResult<LocalMediaVo> pageUrl(Long tagId, Integer current, Integer pageSize,String sortField,String sortOrder) {
        Page<?> page = PageUtils.build(current, pageSize, sortField, sortOrder);
        Page<LocalMediaVo> vo = baseMapper.pageMedia(tagId,page);
        return new PageResult<>(vo);
    }
}




