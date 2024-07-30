package cn.lime.core.service.ocr.ali;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.config.CoreParams;
import com.aliyun.ocr_api20210707.models.RecognizeAdvancedRequest;
import com.aliyun.ocr_api20210707.models.RecognizeAdvancedResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * @ClassName: AliOcrService
 * @Description: 阿里OCR服务
 * @Author: Lime
 * @Date: 2023/12/18 16:52
 */
@Slf4j
@Service
public class AliOcrService implements InitializingBean {


    @Resource
    private CoreParams coreParams;
    private boolean initSuccess;

    /**
     * 客户端
     */
    private com.aliyun.ocr_api20210707.Client client;

    /**
     * 初始化
     * @throws Exception 异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        initSuccess = initSuccess();
        if (!initSuccess){
            log.warn("[Init Ali Ocr] 未检测到Ali Ocr相关配置");
            return;
        }
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(coreParams.getAliOcrAccessId())

                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(coreParams.getAliOcrSecretId());
        // Endpoint 请参考 https://api.aliyun.com/product/ocr-api
        config.endpoint = coreParams.getAliOcrEndPoint();
        client = new com.aliyun.ocr_api20210707.Client(config);
    }

    /**
     * 调用接口
     * @param bodyStream 输入stream
     * @return 解析后数据
     * @throws Exception 异常
     */
    public String doOcr(InputStream bodyStream) {
        ThrowUtils.throwIf(!initSuccess,ErrorCode.INIT_FAIL,"AliOcr功能初始化失败");
        try {
            // 需要安装额外的依赖库，直接点击下载完整工程即可看到所有依赖。
            RecognizeAdvancedRequest recognizeAdvancedRequest = new RecognizeAdvancedRequest()
                    .setBody(bodyStream);
            RuntimeOptions runtime = new RuntimeOptions();
            RecognizeAdvancedResponse resp = client.recognizeAdvancedWithOptions(recognizeAdvancedRequest, runtime);
            return resp.body.getData();
        }catch (Exception e){
            throw new BusinessException(ErrorCode.ALI_OCR_INTERFACE_ERROR,e.getMessage());
        }

    }

    public boolean initSuccess(){
        return StringUtils.isNotEmpty(coreParams.getAliOcrSecretId())
                && StringUtils.isNotBlank(coreParams.getAliOcrAccessId())
                && StringUtils.isNotBlank(coreParams.getAliOcrEndPoint());
    }

}

