package cn.com.msca.service.api.ks;

import cn.com.msca.service.api.ks.dto.res.FaceResultRes;
import cn.com.msca.service.api.ks.dto.res.TokenRes;
import com.alibaba.fastjson2.JSONObject;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.UUID;


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
    private String apiKey;
    @Value("${ks.face.apiSecret}")
    private String apiSecret;
    @Value("${ks.face.faceUrl}")
    private String faceUrl;
    @Value("${ks.face.tokenUrl}")
    private String tokenUrl;
    @Value("${ks.face.returnUrl}")
    private String returnUrl;
    @Value("${ks.face.notifyUrl}")
    private String notifyUrl;
    @Value("${ks.face.comparisonType}")
    private String comparisonType;
    @Value("${ks.face.procedureType}")
    private String procedureType;
    @Value("${ks.face.procedurePriority}")
    private String procedurePriority;
    @Value("${ks.face.sceneId}")
    private String sceneId;
    @Value("${ks.face.resultUrl}")
    private String resultUrl;


    private final WebClient webClient;


    /**
     * 获取活体检测url
     *
     * @return Mono<String> 包含拼接好的活体检测url
     */
    public Mono<String> faceUrlWithTokenReactive() {
        return fetchToken(UUID.randomUUID().toString())
                .elapsed()
                .flatMap(tuple -> {
                    int elapsedMillis = Math.toIntExact(tuple.getT1()); // 获取经过的时间（毫秒）
                    TokenRes tokenRes = tuple.getT2();

                    String token = String.valueOf(tokenRes.getToken());
                    if (StringUtil.isNullOrEmpty(token)) {
                        log.error("获取token失败，token 为空");
                        return Mono.error(new RuntimeException("获取token失败，token 为空"));
                    }

                    // 构建包含活体检测URL的JSON对象
                    JSONObject jsonResponse = new JSONObject();
                    jsonResponse.put("faceUrl", faceUrl + token);
                    jsonResponse.put("code", HttpStatus.OK.value());

                    // 打印成功获取 token 的耗时日志
                    log.info("bizId: {}, 成功获取 token 并构建 URL，耗时: {} ms", UUID.randomUUID(), elapsedMillis);

                    return Mono.just(JSONObject.toJSONString(jsonResponse));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("获取token结果为空")))

                .onErrorResume(e -> {
                    log.error("构建活体检测 URL 失败: {}", e.getMessage());
                    JSONObject errorJsonResponse = new JSONObject();
                    errorJsonResponse.put("error", "构建活体检测 URL 失败: " + e.getMessage());
                    errorJsonResponse.put("code", HttpStatus.FAILED_DEPENDENCY.value());
                    return Mono.error(new RuntimeException(errorJsonResponse.toString()));
                });
    }

    /**
     * 获取token
     *
     * @return
     */
    private Mono<TokenRes> fetchToken(String bizId) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("api_key", apiKey);
        builder.part("api_secret", apiSecret);
        builder.part("return_url", returnUrl);
        builder.part("notify_url", notifyUrl);
        builder.part("biz_no", bizId);
        builder.part("comparison_type", comparisonType);
        builder.part("uuid", bizId);
        builder.part("procedure_type", procedureType);
        builder.part("procedure_priority", procedurePriority);
        builder.part("scene_id", sceneId);
//        builder.part("action_http_method", "GET");
        builder.part("action_http_method", "POST");
        return webClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(TokenRes.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("获取token失败: " + e.getMessage())))
                .log();
    }

    /**
     * 获取活体检测结果
     * @param bizId
     * @return
     */
    public Mono<FaceResultRes> getFaceResult(String bizId) {
        // 使用 UriComponentsBuilder 构建带有查询参数的 URL
        String url = UriComponentsBuilder.fromUriString(resultUrl)
                .queryParam("api_key", apiKey)
                .queryParam("api_secret", apiSecret)
                .queryParam("biz_id", bizId)
                .queryParam("return_verify_time", "1")
                .queryParam("return_image", "1")
                .toUriString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(FaceResultRes.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("获取结果失败: " + e.getMessage())))
                .log();
    }

}
