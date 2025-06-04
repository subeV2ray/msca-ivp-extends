package cn.com.msca.service.bus;

import cn.com.msca.service.api.ks.dto.res.FaceResultRes;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

/**
 * @program: msca-ivp-extends
 * @description: 人脸活体检
 * @author: oiiaioooooiai
 * @create: 2025-06-03 15:10
 **/
public interface FaceIdService {
    Mono<JSONObject> faceUrlWithToken();

    Mono<FaceResultRes> faceResult(String bizId);

    Mono<Void> faceResultCallBack(@RequestParam String data,
                                          @RequestParam String sign,
                                          ServerHttpResponse response);
}
