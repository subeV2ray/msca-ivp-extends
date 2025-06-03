package cn.com.msca.service.api.ks;

import cn.com.msca.service.api.ks.dto.TokenReq;
import cn.com.msca.service.api.ks.dto.TokenRes;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.apache.logging.log4j.CloseableThreadContext.put;

/**
 * @program: msca-ivp-extends
 * @description: 旷视-api
 * @author: oiiaioooooiai
 * @create: 2025-06-03 02:02
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class KsAPI {

    @Value("${ks.face.apiKey}")
    private String account;
    @Value("${ks.face.apiSecret}")
    private String password;
    @Value("${ks.face.faceUrl}")
    private String faceUrl;
    @Value("${ks.face.tokenUrl}")
    private String tokenUrl;
    @Value("${ks.face.returnUrl}")
    private String returnUrl;

    private final WebClient webClient;


    /**
     * 获取token
     *
     * @return
     */
    public Mono<TokenRes> fetchToken() {
        TokenReq tokenReq = new TokenReq();
        // 创建请求体
        tokenReq.setApi_key(account);
        tokenReq.setApi_secret(password);
        tokenReq.setReturn_url(returnUrl);
        tokenReq.setNotify_url(returnUrl);
        tokenReq.setBiz_no(IdUtil.fastSimpleUUID());
        tokenReq.setComparison_type("-1");
        return webClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tokenReq) // 发送 JSON 请求体
                .retrieve()
                .bodyToMono(TokenRes.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("获取token失败 " + e.getMessage())))
                .log();
    }

    public String fetchTokenStr() throws IOException {
        // 使用 Hutool 生成 bizNo
        String bizNo = IdUtil.fastSimpleUUID();

        // 构建表单请求体
        Map<String, String> formBody = new HashMap<>();
        formBody.put("api_key", account);
        formBody.put("api_secret", password);
        formBody.put("return_url", returnUrl);
        formBody.put("notify_url", returnUrl);
        formBody.put("biz_no", bizNo);
        formBody.put("comparison_type", "-1");

        OkHttpClient client = new OkHttpClient();
        // 创建MultipartBody.Builder
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        // 将paramMap中的参数添加到请求体中
        for (Map.Entry<String, String> entry : formBody.entrySet()) {
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }
        // 构造请求体
        RequestBody body = builder.build();
        // 创建请求对象
        Request request = new Request.Builder()
                .url(tokenUrl)
                .post(body)
                .addHeader("Host", "tapi.megvii.com")
                .build();
        // 执行请求

        // 执行请求并获取响应
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                log.info("获取 token 成功: {}", responseBody);
                return responseBody;
            } else {
                throw new RuntimeException("请求 token 失败，HTTP 状态码: " + response.code());
            }
        }
    }


    /**
     * 获取活体检测url
     *
     * @return Mono<String> 包含拼接好的活体检测url
     */
    public Mono<String> faceUrlWithTokenReactive() {
        return fetchToken() // 获取 Mono<TokenRes>
                .flatMap(tokenRes -> { // 使用 flatMap 转换 Mono<TokenRes> 为 Mono<String>
                    String token = String.valueOf(tokenRes.getTime_used());
                    if (StringUtil.isNullOrEmpty(token)) { // 使用 Guava 的 Strings.isNullOrEmpty
                        log.error("获取token失败，token 为空");
                        // 如果 token 为空，抛出业务异常或返回一个空的 Mono
                        return Mono.error(new RuntimeException("获取token失败，token 为空"));
                    }
                    // 返回拼接的活体检测url
                    return Mono.just(faceUrl + token);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("获取token结果为空"))) // 处理 fetchToken 返回空 Mono 的情况
                .onErrorResume(e -> { // 统一处理错误
                    log.error("构建活体检测 URL 失败: {}", e.getMessage());
                    return Mono.error(new RuntimeException("构建活体检测 URL 失败: " + e.getMessage()));
                });
    }
}
