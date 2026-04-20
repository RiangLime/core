package cn.lime.core._config;

import cn.lime.core._ai.config.AiProperties;
import cn.lime.core._aes.config.AesProperties;
import cn.lime.core._media.config.LocalMediaProperties;
import cn.lime.core._media.config.QiNiuProperties;
import cn.lime.core._ocr.config.AliOcrProperties;
import cn.lime.core._sms.config.AliSmsProperties;
import cn.lime.core._snowflake.config.SnowflakeProperties;
import cn.lime.core._login.config.TokenProperties;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CoreParams {

    @Resource
    private TokenProperties tokenProperties;
    @Resource
    private SnowflakeProperties snowflakeProperties;
    @Resource
    private AesProperties aesProperties;
    @Resource
    private LocalMediaProperties localMediaProperties;
    @Resource
    private QiNiuProperties qiNiuProperties;
    @Resource
    private AiProperties aiProperties;
    @Resource
    private AliOcrProperties ocrProperties;
    @Resource
    private AliSmsProperties aliSmsProperties;
    @Resource
    private WxProperties wxProperties;


}
