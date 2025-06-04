package cn.com.msca.controller;

import cn.com.msca.service.api.ks.dto.res.FaceResultRes;
import cn.com.msca.service.bus.FaceIdService;
import cn.com.msca.util.StringUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @program: msca-ivp-extends
 * @description: 旷视-cotroller
 * @author: oiiaioooooiai
 * @create: 2025-06-03 15:05
 **/
@RequiredArgsConstructor
@RestController
@RequestMapping("/faceId")
public class FaceIdController {

    private final FaceIdService faceIdService;


    /**
     * 获取活体检测url
     * @return
     */
    @GetMapping("/getFaceIdUrl")
    public Mono<JSONObject> getFaceIdUrl() {
        return faceIdService.faceUrlWithToken();
    }

    /**
     * 获取活体检测结果
     * @param bizId
     * @return
     */
    @GetMapping("/getFaceResult/{bizId}")
    public Mono<FaceResultRes> getFaceResult(@PathVariable("bizId") String bizId) {
        return faceIdService.faceResult(bizId);
    }

    /**
     * 活体检测结果回调
     * @param data
     * @param sign
     * @param response
     * @return
     */
    @PostMapping("/faceResultCallBack")
    private Mono<Void> faceResultCallBack(@RequestParam String data,
                                          @RequestParam String sign,
                                          ServerHttpResponse response) {
        return faceIdService.faceResultCallBack(data, sign, response);
    }
}
