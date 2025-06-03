package cn.com.msca.service.bus.impl;

import cn.com.msca.service.api.ks.KsAPI;
import cn.com.msca.service.api.ks.dto.TokenReq;
import cn.com.msca.service.api.ks.dto.TokenRes;
import cn.com.msca.service.bus.FaceIdService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @program: msca-ivp-extends
 * @description: 人脸活体检测-impl
 * @author: oiiaioooooiai
 * @create: 2025-06-03 15:11
 **/
@Service
public class FaceIdServiceImpl implements FaceIdService {

    @Resource
    private KsAPI ksAPI;

    @Override
    public Mono<String> faceUrlWithToken() {
        return ksAPI.faceUrlWithTokenReactive();
    }

    @Override
    public Mono<TokenRes> fetchToken() {
        return ksAPI.fetchToken();
    }

    @Override
    public String fetchTokenStr() throws IOException {
        return ksAPI.fetchTokenStr();
    }
}
