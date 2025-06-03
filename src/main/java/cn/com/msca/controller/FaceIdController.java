package cn.com.msca.controller;

import cn.com.msca.service.api.ks.dto.TokenRes;
import cn.com.msca.service.bus.FaceIdService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;

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

    @Resource
    private FaceIdService faceIdService;

    /**
     * 获取活体检测url
     * @return
     */
    @GetMapping("/getFaceIdUrl")
    public Mono<String> getFaceIdUrl() {
        return faceIdService.faceUrlWithToken();
    }

    @GetMapping("/getToken")
    public Mono<TokenRes> getToken() {
        return faceIdService.fetchToken();
    }


    @GetMapping("/fetchTokenStr")
    public String fetchTokenStr() throws IOException {
        return faceIdService.fetchTokenStr();
    }

}
