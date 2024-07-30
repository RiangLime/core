package cn.lime.core.module.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @ClassName: AppKeySecretInfo
 * @Description: 应用的公钥秘钥
 * @Author: Lime
 * @Date: 2023/12/18 14:50
 */
@Data
@AllArgsConstructor
public class AppKeySecretInfo {

    private String appKey;
    private String appSecret;

    
}
