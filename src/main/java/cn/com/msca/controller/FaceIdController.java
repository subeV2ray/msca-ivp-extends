package cn.com.msca.controller;

import cn.com.msca.service.api.ks.dto.req.FaceCallbackRequest;
import cn.com.msca.service.api.ks.dto.res.FaceResultRes;
import cn.com.msca.service.bus.FaceIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @program: msca-ivp-extends
 * @description: 旷视-cotroller
 * @author: oiiaioooooiai
 * @create: 2025-06-03 15:05
 **/
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/faceId")
public class FaceIdController {

    private final FaceIdService faceIdService;


    /**
     * 获取活体检测url
     *
     * @return
     */
    @GetMapping("/getFaceIdUrl")
    public Mono<String> getFaceIdUrl() {
        return faceIdService.faceUrlWithToken();
    }

    /**
     * 获取活体检测结果
     *
     * @param bizId
     * @return
     */
    @GetMapping("/getFaceResult/{bizId}")
    public Mono<FaceResultRes> getFaceResult(@PathVariable("bizId") String bizId) {
        return faceIdService.faceResult(bizId);
    }

    /**
     * 活体检测结果回调
     *
     * @param response
     * @return
     */
//    @GetMapping("/faceCallBack")
    @PostMapping(value = "/faceCallBack", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Void> faceResultCallBack(@ModelAttribute FaceCallbackRequest faceCallbackRequest,
                                         ServerHttpResponse response) {

        String data = faceCallbackRequest.getData();
        String sign = faceCallbackRequest.getSign();
        log.info("data: {}, sign: {}", data, sign);

        return faceIdService.faceResultCallBack(data, sign, response);
    }


    @PostMapping(value = "/notify", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Mono<Void> notify(String data,
                             String sign) {

        log.info("data: {}, sign: {}, res: {}", data, sign, null);
        return Mono.empty();
    }
}
