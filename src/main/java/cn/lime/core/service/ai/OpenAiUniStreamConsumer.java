package cn.lime.core.service.ai;

import cn.lime.core.constant.Symbol;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncByteConsumer;
import org.apache.http.protocol.HttpContext;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @ClassName: AiTalkStreamConsumer
 * @Description: AI交流流输出定制化消费者
 * @Author: Lime
 * @Date: 2023/11/29 13:28
 */
@Slf4j
public class OpenAiUniStreamConsumer extends AsyncByteConsumer<Boolean> {

    private final StringBuilder resultBuilder;

    /**
     * 第一DATA assistant 非数据字段 不计算在token数量中
     * 倒数第二个DATA finish 非数据字段 不计算在token数量中
     * 最后一个DATA DONE 非数据字段 不计算在token数量中
     */
    private int resultTokenNumber = -3;

    public OpenAiUniStreamConsumer(ResponseBodyEmitter emitter, StringBuilder resultBuilder) {
        this.emitter = emitter;
        this.resultBuilder = resultBuilder;
    }

    private final ResponseBodyEmitter emitter;

    @Override
    protected void onResponseReceived(final HttpResponse response) {
    }


    @Override
    protected void onByteReceived(ByteBuffer buf, IOControl ioControl) throws IOException {
        List<Byte> byteList = new ArrayList<>();

        while (buf.hasRemaining()) {
            byte byteB = buf.get();
            byteList.add(byteB);
            byte[] bytes = new byte[byteList.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = byteList.get(i);
            }
            String content = IOUtils.toString(bytes, String.valueOf(UTF_8));
            log.info(content);
            if (content.endsWith((Symbol.CHANGE_LINE + "" + Symbol.CHANGE_LINE))) {
                emitter.send(content);
                log.info("ai return <- {}", content);
                resultTokenNumber++;
                byteList = new ArrayList<>();
                resultBuilder.append(content);
            }
        }
    }

    @Override
    protected Boolean buildResult(final HttpContext context) throws IOException {
        log.info("[FULL] {}",resultBuilder.toString());
        return Boolean.TRUE;
    }

    public int getResultTokenNumber() {
        return resultTokenNumber;
    }
}
