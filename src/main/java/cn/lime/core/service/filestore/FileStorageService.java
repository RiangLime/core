package cn.lime.core.service.filestore;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.config.CoreParams;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName: FileStorageService
 * @Description: TODO
 * @Author: Lime
 * @Date: 2024/7/30 17:24
 */
@Service
@Slf4j
public class FileStorageService implements InitializingBean {

    @Resource
    private CoreParams coreParams;

    private boolean initSuccess;

    @Override
    public void afterPropertiesSet() throws Exception {
        initSuccess = initSuccess();
        if (initSuccess) {
            File storageDir = new File(coreParams.getFileStoragePath());
            if (!storageDir.exists()) {
                if (!storageDir.mkdirs()) {
                    initSuccess = false;
                    log.warn("[Init File Storage] 多媒体目录创建失败");
                }
            }
        } else {
            log.warn("[Init File Storage] 未检测到File Storage相关配置");
        }
    }

    public boolean initSuccess() {
        return StringUtils.isNotEmpty(coreParams.getFileStoragePath())
                && StringUtils.isNotBlank(coreParams.getFileStoragePath());
    }

    public String uploadFile(MultipartFile file) {
        ThrowUtils.throwIf(!initSuccess, ErrorCode.INIT_FAIL, "多媒体服务器初始化失败");
        String originalFileName = file.getOriginalFilename();
        ThrowUtils.throwIf(StringUtils.isEmpty(originalFileName), ErrorCode.PARAMS_ERROR, "文件名为空");
        File localFile = new File(coreParams.getFileStoragePath() + File.separator + originalFileName);
        // 获取文件的基本名称和扩展名
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        if (localFile.exists()) {
            // 从1 开始依次检查文件名
            int counter = 1;
            // 循环检查文件是否存在
            while (localFile.exists()) {
                // 生成带序号的新文件名
                String newFileName = baseName + "_" + counter + extension;
                localFile = new File(newFileName);
                counter++;
            }
        }
        try (FileOutputStream fos = new FileOutputStream(localFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.IO_ERROR, "文件落盘异常");
        }
        return localFile.getAbsolutePath().replace(coreParams.getFileStoragePath(), coreParams.getFileStorageUrlPrefix());
    }

    public void deleteFile(String url) {
        ThrowUtils.throwIf(!initSuccess, ErrorCode.INIT_FAIL, "多媒体服务器初始化失败");
        String realFilePath = url.replace(coreParams.getFileStorageUrlPrefix(), coreParams.getFileStoragePath());
        File realFile = new File(realFilePath);
        if (realFile.exists()) {
            FileUtils.deleteQuietly(realFile);
        }
    }

}
