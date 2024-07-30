package cn.lime.core.service.ai;

import cn.lime.core.common.ErrorCode;
import cn.lime.core.common.StreamClientException;
import cn.lime.core.service.ai.bean.OpenAiMessageBean;
import com.google.gson.Gson;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.AbstractAsyncResponseConsumer;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @ClassName: OpenAiUniStreamClient
 * @Description: TODO
 * @Author: Lime
 * @Date: 2023/12/20 14:53
 */
public class OpenAiUniStreamClient {

    private final String token;

    private final String url;

    private final List<OpenAiMessageBean> messages;

    private final double temperature;

    private final String module;

    private final AbstractAsyncResponseConsumer<Boolean> consumer;

    public OpenAiUniStreamClient(String token, String url, List<OpenAiMessageBean> messages, double temperature,
                                 String module,AbstractAsyncResponseConsumer<Boolean> consumer) {
        this.token = token;
        this.url = url;
        this.messages = messages;
        this.temperature = temperature;
        this.module = module;
        this.consumer = consumer;
    }

    public void callAi() {
        Map<Object, Object> payload = new HashMap<>();
        List<Map<Object, Object>> messageList = new ArrayList<>();

        for (OpenAiMessageBean message : messages) {
            messageList.add(Map.of("role", message.getRole(), "content", message.getContent()));
        }

        payload.put("messages", messageList);
        payload.put("model", module);
        payload.put("stream", true);
        payload.put("temperature", temperature);

        String reqParam = new Gson().toJson(payload);
        System.out.println(reqParam);

        try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();) {
            httpclient.start();

            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Authorization", "Bearer " + token);
            StringEntity reqEntity = new StringEntity(new Gson().toJson(payload), "utf-8");
            reqEntity.setContentEncoding("UTF-8");
            reqEntity.setContentType("application/json");
            post.setEntity(reqEntity);


            HttpAsyncRequestProducer producer = HttpAsyncMethods.create(post);

            Future<Boolean> future = httpclient.execute(producer, consumer, null);
            Boolean result = future.get();
            if (result == null || !result) {
                throw new StreamClientException(ErrorCode.OPEN_AI_INTERFACE_ERROR);
            }
        } catch (Exception e) {
            throw new StreamClientException(ErrorCode.OPEN_AI_INTERFACE_ERROR, e.getMessage());
        }
    }

}
