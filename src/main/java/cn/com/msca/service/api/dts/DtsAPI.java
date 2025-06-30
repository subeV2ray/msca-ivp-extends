package cn.com.msca.service.api.dts;

import cn.com.msca.service.api.dts.dto.threeElements.ThreeElementReq;
import cn.com.msca.service.api.dts.dto.threeElements.ThreeElementRes;
import cn.com.msca.service.api.dts.dto.token.TokenReq;
import cn.com.msca.service.api.dts.dto.token.TokenRes;
import cn.com.msca.service.api.dts.dto.twoElemments.TwoElementsReq;
import cn.com.msca.service.api.dts.dto.twoElemments.TwoElementsRes;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @program: msca-ivp-extends
 * @description: dts-api
 * @author: oiiaioooooiai
 * @create: 2025-06-30 16:04
 **/
@RequiredArgsConstructor
@Service
@Slf4j
public class DtsAPI {

    @Value("${dts.app-id}")
    private String appId;
    @Value("${dts.app-secret}")
    private String appSecret;

    @Value("${dts.token-url}")
    private String tokenUrl;

    @Value("${dts.two-element-url}")
    private String twoElementUrl;
    @Value("${dts.three-element-url}")
    private String threeElementUrl;


    private final WebClient webClient;


    /**
     * 获取token
     *
     * @return
     */
    public Mono<TokenRes> getToken() {
        return webClient.post()
                .uri(tokenUrl)
                .body(Mono.just(new TokenReq(appId, appSecret)), TokenReq.class)
                .retrieve()
                .bodyToMono(TokenRes.class)
                .doOnError(e -> log.error("获取token失败:", e))
                .<TokenRes>handle((tokenRes, sink) -> {
                    if (tokenRes == null || tokenRes.getData().getToken() == null) {
                        sink.error(new RuntimeException("获取到的token为空"));
                        return;
                    }
                    log.info("获取到的token: {}", JSONObject.toJSONString(tokenRes));
                    sink.next(tokenRes);
                })
                .onErrorResume(e -> Mono.error(new RuntimeException("获取token失败: " + e.getMessage(), e)))
                .log("TokenFlow");
    }



    /**
     * 个人二要素
     *
     * @param twoElementsReq
     * @return
     */
    public Mono<TwoElementsRes> twoElements(TwoElementsReq twoElementsReq) {
        return getToken()
                .flatMap(tokenRes -> webClient.post()
                        .uri(twoElementUrl)
                        .header("token", tokenRes.getData().getToken())
                        .body(Mono.just(twoElementsReq), TwoElementsReq.class)
                        .retrieve()
                        .bodyToMono(TwoElementsRes.class))
                .onErrorResume(e -> Mono.error(new RuntimeException("二要素验证失败: " + e.getMessage())))
                .log();
    }


    /**
     * 个人三要素
     * @param threeElementsReq
     * @return
     */
    public Mono<ThreeElementRes> threeElements(ThreeElementReq threeElementsReq) {
        return getToken()
                .flatMap(tokenRes -> webClient.post()
                        .uri(threeElementUrl)
                        .header("token", tokenRes.getData().getToken())
                        .body(Mono.just(threeElementsReq), ThreeElementReq.class)
                        .retrieve()
                        .bodyToMono(ThreeElementRes.class))
                .onErrorResume(e -> Mono.error(new RuntimeException("三要素验证失败: " + e.getMessage())))
                .log();
    }


}
