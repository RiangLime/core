package cn.lime.core.service.ai;

import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.ThrowUtils;
import cn.lime.core.config.CoreParams;
import cn.lime.core.service.ai.bean.OpenAiRequest;
import cn.lime.core.service.ai.bean.OpenAiResponse;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: UniAiService
 * @Description: 统一AI服务
 * @Author: Lime
 * @Date: 2023/12/20 14:36
 */
@Service
@Slf4j
public class UniAiService implements InitializingBean {

    @Resource
    private CoreParams coreParams;
    private boolean initSuccess;

    /**
     * 流式请求使用线程池
     */
    // AI任务执行线程池
    private static final ExecutorService TASK_POOL = new ThreadPoolExecutor(5, 15,
            1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public void afterPropertiesSet() throws Exception {
        initSuccess = initSuccess();
        if (!initSuccess){
            log.warn("[Init AI] 未检测到OpenAI相关配置");
        }
    }

    /**
     * 执行AI任务 调用OpenAI接口访问
     *
     * @param request 对外请求体
     * @param emitter 响应返回emitter
     */
    public void doAiTask(OpenAiRequest request, ResponseBodyEmitter emitter) {
        ThrowUtils.throwIf(!initSuccess,ErrorCode.INIT_FAIL,"AI功能初始化失败");
        StringBuilder aiResultBuilder = new StringBuilder();
        try {
            // 流式传输
            if (request.getStream()) {
                // log inside consumer
                // nothing will be recorded since the AI task run in async mode
                doSyncStream(request, coreParams.getAiKey(), coreParams.getAiUrl(), emitter, aiResultBuilder);
            }
            // 正常同步返回
            else {
                OpenAiResponse response = doSync(request, coreParams.getAiKey(), coreParams.getAiUrl());
                emitter.send(JSON.toJSONString(response), MediaType.TEXT_PLAIN);
                emitter.complete();
            }
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

    }

    /**
     * 正常同步访问OpenAI API
     *
     * @param request   外部请求体
     * @param realToken 业务映射后的token
     * @param realUrl   业务映射后的URL
     * @return AI响应
     */
    private OpenAiResponse doSync(OpenAiRequest request, String realToken, String realUrl) {
        try {
            return new OpenAiUniClient(realToken, realUrl, request.getMessages(), request.getTemperature(), request.getModel()).callAi();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPEN_AI_INTERFACE_ERROR, e.getMessage());
        }
    }

    /**
     * 开始流式开关访问OpenAI API
     *
     * @param request       对外DTO
     * @param token         业务颁发token
     * @param url           实际调用url
     * @param emitter       返回数据
     * @param resultBuilder 结果数据
     */
    private void doSyncStream(OpenAiRequest request, String token, String url, ResponseBodyEmitter emitter,
                              StringBuilder resultBuilder) {
        TASK_POOL.execute(() -> {
            try {
                OpenAiUniStreamConsumer consumer = new OpenAiUniStreamConsumer(emitter, resultBuilder);
                new OpenAiUniStreamClient(token, url, request.getMessages(), request.getTemperature(),
                        request.getModel(), consumer).callAi();
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
    }

    public boolean initSuccess(){
        return StringUtils.isNotEmpty(coreParams.getAiKey()) && StringUtils.isNotBlank(coreParams.getAiUrl());
    }

}
