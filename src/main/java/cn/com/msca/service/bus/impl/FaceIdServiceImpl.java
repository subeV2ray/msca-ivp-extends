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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
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
    @Value("${ks.face.apiSecret}")
    private String apiSecret;
    @Value("${ks.face.returnUrl}")
    private String returnUrl;
    String url = "http://183.66.184.22:19523/nginx/dist/#";
    /**
     * 获取人脸url和token
     * @return
     */
    @Override
    public Mono<String> faceUrlWithToken() {
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
     * @return
     */
    @Override
    public Mono<Void> faceResultCallBack(String data, String sign, ServerHttpResponse response)  {

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
            response.getHeaders().setLocation(URI.create(url));
            return response.setComplete();
        }
        JSONObject bizInfo = result.getBiz_info();
        String bizId = bizInfo.getString("biz_id");


        response.setStatusCode(HttpStatus.FOUND);


        return faceResult(bizId)
                .flatMap(faceResultRes -> {
                    // 获取图片
                    String image = faceResultRes.getImages().getString("image_best");

                    return sjbAPI.faceThreeElements(image, "吴秋松", "500101199909164412")
                            .flatMap(res -> {
                                JSONObject faceThreeElements = JSONObject.parseObject(res);
                                // 成功则重定向成功页面
                                if (StringUtil.equals(faceThreeElements.getString("code"), "10000")) {

                                    JSONObject resultInfo = faceThreeElements.getJSONObject("data");
                                    Double score = resultInfo.getDouble("score");
                                    String message  = resultInfo.getString("message");

                                    // 拼接

                                    if (score >= 0.7) {
                                        // 认证通过
                                        url = url  + "faceDetail?message=" + message + "&passed=" + true;
                                        response.getHeaders().setLocation(URI.create(url));
                                    } else {
                                        // 认证不通过
                                        url = url  + "faceDetail?message=" + message + "&passed=" + false;
                                        response.getHeaders().setLocation(URI.create(url));
                                    }
                                    return response.setComplete();
                                }

                                // 失败
                                url = url  + "faceDetail?message=" + "FAILED" + "&passed=" + false;
                                response.getHeaders().setLocation(URI.create(url));
                                return response.setComplete();
                            });

                });
    }


}
