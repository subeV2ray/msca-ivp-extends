package cn.com.msca.service.bus.impl;

import cn.com.msca.service.api.ks.KsAPI;
import cn.com.msca.service.api.ks.dto.res.FaceResultRes;
import cn.com.msca.service.api.sjb.SjbAPI;
import cn.com.msca.service.bus.FaceIdService;
import cn.com.msca.util.StringUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.handler.codec.http.HttpResponse;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @program: msca-ivp-extends
 * @description: 人脸活体检测-impl
 * @author: oiiaioooooiai
 * @create: 2025-06-03 15:11
 **/
@Service
@RequiredArgsConstructor
public class FaceIdServiceImpl implements FaceIdService {

    private final KsAPI ksAPI;
    private final SjbAPI sjbAPI;
    @Value("${ks.apiSecret}")
    private String apiSecret;

    /**
     * 获取人脸url和token
     * @return
     */
    @Override
    public Mono<JSONObject> faceUrlWithToken() {
        return ksAPI.faceUrlWithTokenReactive();
    }

    /**
     * 获取人脸结果
     * @param bizId
     * @return
     */
    @Override
    public Mono<FaceResultRes> faceResult(String bizId) {
        return ksAPI.getFaceResult(bizId);
    }

    /**
     * 人脸结果回调
     * @param bizId
     * @param httpResponse
     * @return
     */
    @Override
    public Mono<Void> faceResultCallBack(@RequestParam String data,
                                          @RequestParam String sign,
                                          ServerHttpResponse response)  {

        // 验签
        String expectedSign = DigestUtil.sha1Hex(apiSecret + data);
        if (!expectedSign.equals(sign)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        // 处理 JSON 内容
        FaceResultRes result = JSON.parseObject(data, FaceResultRes.class);
        boolean pass = StringUtil.equals(result.getLiveness_result().getString("result"), "PASS");
        if (!pass) {
            // 根据业务逻辑处理成功后重定向
            response.setStatusCode(HttpStatus.FAILED_DEPENDENCY);
            response.getHeaders().setLocation(URI.create("https://google.com/"));
            return response.setComplete();
        }


        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create("https://google.com"));
        return response.setComplete();
    }


}
