package cn.lime.core.service.oss;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.config.CoreParams;
import cn.lime.core.module.bean.OssUploadRsp;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @ClassName: QiNiuOssService
 * @Description: 七牛OSS服务
 * @Author: Lime
 * @Date: 2023/12/18 16:47
 */
@Service
@Slf4j
public class QiNiuOssService {

    @Resource
    private CoreParams coreParams;

    public String generateQiNiuToken(String bucket, String key) {
        Auth auth = Auth.create(coreParams.getQiNiuOssAppId(), coreParams.getQiNiuOssSecretId());
        return auth.uploadToken(bucket, key, Long.parseLong(coreParams.getQiNiuOssTokenExpire()), new StringMap(), true);

    }


    public OssUploadRsp uploadFile(byte[] fileBytes,String bucket, String fileKey) {

        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        log.info("[QINIU] key:[{}] upload to bucket [{}]", fileKey, bucket);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(coreParams.getQiNiuOssAppId(), coreParams.getQiNiuOssSecretId());
        String upToken = auth.uploadToken(bucket, fileKey);
        try {
            Response response = uploadManager.put(fileBytes, fileKey, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return new OssUploadRsp(putRet.key, null);
        } catch (QiniuException ex) {
            throw new BusinessException(ErrorCode.QI_NIU_OSS_INTERFACE_ERROR, ex.getMessage());
        }
    }

    public OssUploadRsp uploadFile(File file, String bucket, String fileKey) {
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        // ...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        Auth auth = Auth.create(coreParams.getQiNiuOssAppId(), coreParams.getQiNiuOssSecretId());
        String upToken = auth.uploadToken(bucket, fileKey);
        try {
            Response response = uploadManager.put(file.getAbsoluteFile(), fileKey, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            return new OssUploadRsp(putRet.key, null);
        } catch (QiniuException ex) {
            throw new BusinessException(ErrorCode.QI_NIU_OSS_INTERFACE_ERROR, ex.getMessage());
        }
    }

    public void deleteRemote(String bucket, String key) throws QiniuException {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        Auth auth = Auth.create(coreParams.getQiNiuOssAppId(), coreParams.getQiNiuOssSecretId());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        bucketManager.delete(bucket, key);

    }

}
