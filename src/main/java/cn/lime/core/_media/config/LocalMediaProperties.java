package cn.lime.core._media.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName: LocalMediaProperties
 * @Description: 本地多媒体服务器
 * @Author: riang
 * @Date: 2026/4/20 17:49
 */
@Data
@Component
public class LocalMediaProperties {
    // file storage
    @Value("${core.file-storage.path:/home/ubuntu/uploads}")
    private String fileStoragePath;
    @Value("${core.file-storage.prefix:https://java.shop.ceagull.top/upload}")
    private String fileStorageUrlPrefix;

    // file storage
    @Value("${core.avatar-storage.path:/home/ubuntu/uploads_avatar}")
    private String avatarStoragePath;
    @Value("${core.avatar-storage.prefix:https://java.shop.ceagull.top/upload}")
    private String avatarStorageUrlPrefix;
    @Value("${core.avatar-storage.max-size-KB:500}")
    private Integer avatarMaxSizeKB;
}