package cn.lime.core.service.ai;

import cn.hutool.http.HttpStatus;
import cn.lime.core.common.BusinessException;
import cn.lime.core.common.ErrorCode;
import cn.lime.core.service.ai.bean.OpenAiMessageBean;
import cn.lime.core.service.ai.bean.OpenAiResponse;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: OpenAiClient
 * @Description: TODO
 * @Author: Lime
 * @Date: 2023/12/20 14:42
 */
public class OpenAiUniClient {

    private final String token;

    private final String url;

    private final List<OpenAiMessageBean> messages;

    private final double temperature;

    private final String module;

    public OpenAiUniClient(String token, String url, List<OpenAiMessageBean> messages, double temperature, String module) {
        this.token = token;
        this.url = url;
        this.messages = messages;
        this.temperature = temperature;
        this.module = module;
    }

    public OpenAiResponse callAi() throws IOException, InterruptedException {
        // Build the request payload
        Map<Object, Object> payload = new HashMap<>();
        List<Map<Object, Object>> messageList = new ArrayList<>();
        for (OpenAiMessageBean message : messages) {
            messageList.add(Map.of("role", message.getRole(), "content", message.getContent()));
        }

        payload.put("messages", messageList);
        payload.put("model", module);
        payload.put("stream", false);
        payload.put("temperature", temperature);
//        payload.put("allow_fallback", true);
        // Create HttpClient
        HttpClient httpClient = HttpClient.newHttpClient();

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .timeout(Duration.ofMinutes(3))
                .POST(buildJsonFromMap(payload))
                .build();
        // Send the request and get the response
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Print the response
        if (response.statusCode() != HttpStatus.HTTP_OK) {
            throw new BusinessException(ErrorCode.OPEN_AI_INTERFACE_ERROR, response.body());
        }
        return JSON.parseObject(response.body(), OpenAiResponse.class);
    }

    private static HttpRequest.BodyPublisher buildJsonFromMap(Map<Object, Object> map) {
        String json = new Gson().toJson(map);
        return HttpRequest.BodyPublishers.ofString(json);
    }
}
