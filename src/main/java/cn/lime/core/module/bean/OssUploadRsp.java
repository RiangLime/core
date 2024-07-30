package cn.lime.core.module.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: OssUploadRsp
 * @Description: OSS上传响应
 * @Author: Lime
 * @Date: 2024/3/18 17:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OssUploadRsp {
    private String fileKey;
    private String fileUrl;
}
