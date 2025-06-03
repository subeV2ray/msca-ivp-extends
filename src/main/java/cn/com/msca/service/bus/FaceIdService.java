package cn.com.msca.service.bus;

import cn.com.msca.service.api.ks.dto.TokenRes;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @program: msca-ivp-extends
 * @description: 人脸活体检
 * @author: oiiaioooooiai
 * @create: 2025-06-03 15:10
 **/
public interface FaceIdService {
    Mono<String> faceUrlWithToken();

    Mono<TokenRes> fetchToken();
    String fetchTokenStr() throws IOException;

}
