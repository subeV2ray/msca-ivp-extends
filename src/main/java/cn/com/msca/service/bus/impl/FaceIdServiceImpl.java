package cn.com.msca.service.bus.impl;

import cn.com.msca.service.api.ks.KsAPI;
import cn.com.msca.service.api.ks.dto.res.FaceResultRes;
import cn.com.msca.service.api.sjb.SjbAPI;
import cn.com.msca.service.bus.FaceIdService;
import cn.com.msca.util.StringUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @program: msca-ivp-extends
 * @description: 人脸活体检测-impl
 * @author: oiiaioooooiai
 * @create: 2025-06-03 15:11
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class FaceIdServiceImpl implements FaceIdService {

    private final KsAPI ksAPI;
    private final SjbAPI sjbAPI;
    @Value("${ks.face.apiSecret}")
    private String apiSecret;
    @Value("${ks.face.returnUrl}")
    private String returnUrl;
    String url = "https://gptrqnxa7u.by.takin.cc/";

    /**
     * 获取人脸url和token
     *
     * @return
     */
    @Override
    public Mono<String> faceUrlWithToken() {
        return ksAPI.faceUrlWithTokenReactive();
    }

    /**
     * 获取人脸结果
     *
     * @param bizId
     * @return
     */
    @Override
    public Mono<FaceResultRes> faceResult(String bizId) {
        return ksAPI.getFaceResult(bizId);
    }

    /**
     * 人脸结果回调
     *
     * @return
     */
    @Override
    public Mono<Void> faceResultCallBack(String data, String sign, ServerHttpResponse response) {
        // 验签
        String expectedSign = DigestUtil.sha1Hex(apiSecret + data);
        if (!expectedSign.equals(sign)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        FaceResultRes result = JSON.parseObject(data, FaceResultRes.class);
//        String name = result.getIdcard_info().getString("name");
//        String idCard = result.getIdcard_info().getString("idcard");
        boolean pass = StringUtil.equals(result.getLiveness_result().getString("result"), "PASS");


        if (!pass) {
            String redirectUrl = buildRedirectUrl(url, "吴秋松", false);
            return redirect(response, redirectUrl);
        }

        String bizId = result.getBiz_info().getString("biz_id");

        return faceResult(bizId)
                .flatMap(faceResultRes -> {
                    String image = faceResultRes.getImages().getString("image_best");
                    return sjbAPI.faceThreeElements(image, "吴秋松", "500101199909164412")
                            .flatMap(res -> {
                                JSONObject faceThreeElements = JSONObject.parseObject(res);
                                if (!"10000".equals(faceThreeElements.getString("code"))) {
                                    String redirectUrl = buildRedirectUrl(url, "吴秋松", false);
                                    return redirect(response, redirectUrl);
                                }

                                JSONObject dataObj = faceThreeElements.getJSONObject("data");
                                Double score = dataObj.getDouble("score");
                                String message = dataObj.getString("message");
                                boolean passed = score != null && score >= 0.7;

                                String redirectUrl = buildRedirectUrl(url, message, passed);
                                return redirect(response, redirectUrl);
                            });
                })
                .onErrorResume(e -> {
                    log.error("人脸结果回调失败: ", e);
                    String redirectUrl = buildRedirectUrl(url, "吴秋松", false);
                    return redirect(response, redirectUrl);
                })
                .log();
    }

    private String buildRedirectUrl(String baseUrl, String message, boolean passed) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("message", message)
                .queryParam("passed", passed)
                .toUriString();
    }

    private Mono<Void> redirect(ServerHttpResponse response, String url) {
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create(url));
        return response.setComplete();
    }


}
