package cn.com.msca.service.api.sjb;

import cn.com.msca.util.Base64Encoder;
import cn.com.msca.util.DateFormatUtil;
import com.alibaba.fastjson2.JSONObject;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @program: msca-ivp-extends
 * @description: 数据宝-api
 * @author: oiiaioooooiai
 * @create: 2025-06-04 13:40
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class SjbAPI {

    @Value("${sjb.personal-url}")
    private String imgUrl;
    @Value("sjb.api-key")
    private String key;

    private final WebClient webClient;


    /**
     * 人脸三要素认证
     * @param name
     * @param idCard
     * @return
     */
    public Mono<String> faceThreeElements(String base64Image, String name, String idCard) {
        if (StringUtil.isNullOrEmpty(name) || StringUtil.isNullOrEmpty(idCard)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        return imgUpload(base64Image)
                .flatMap(uploadResponse -> {
                    // 解析上传响应 JSON
                    JSONObject uploadJson = JSONObject.parseObject(uploadResponse);
                    String imageId = uploadJson.getString("data"); // 提取 data 字段

                    if (StringUtil.isNullOrEmpty(imageId)) {
                        return Mono.error(new RuntimeException("图片上传响应中缺少 'data' 字段"));
                    }

                    // 构建三要素认证请求体
                    MultipartBodyBuilder builder = new MultipartBodyBuilder();
                    builder.part("key", key);
                    builder.part("name", name);
                    builder.part("idcard", idCard);
                    builder.part("imageId", imageId);

                    return webClient.post()
                            .uri(imgUrl)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromMultipartData(builder.build()))
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnSuccess(response ->
                                    log.info("数据宝-人脸三要素认证成功，响应: {}",
                                            response.substring(0, Math.min(200, response.length())) + "..."))
                            .log();
                });
    }


    /**
     * 图片上传
     *
     * @param base64Image
     * @return
     */
    private Mono<String> imgUpload(String base64Image) {
        // 构建最终的 POST URL，包含 appkey 和可选的 callback
        String POST_URL = imgUrl + "?appkey=" + key;

        String timestamp = String.valueOf(DateFormatUtil.getNowTime());
        // 假设图片默认是 JPEG 格式。如果需要支持其他格式，可能需要从 Base64 前缀或额外参数中判断
        String originalFilename = "faceId_" + timestamp + ".jpg"; // 默认使用 .jpeg 后缀

        log.info("开始上传图片，Base64 字符串长度: {}, 文件名: {}", base64Image.length(), originalFilename);

        // 1. Base64 解码：将 Base64 字符串转换为字节数组
        byte[] bytes;
        try {
            long decodeStartTime = System.currentTimeMillis();
            // 调用内部的解码方法
            bytes = Base64Encoder.decodeToBytes(base64Image);
            long decodeEndTime = System.currentTimeMillis();
            log.info("Base64 解码耗时: {}ms, 解码后字节长度: {}", (decodeEndTime - decodeStartTime), bytes.length);
        } catch (IllegalArgumentException e) {
            log.error("Base64 图片解码失败: {}", e.getMessage());
            return Mono.error(new RuntimeException("图片数据无效，无法解码: " + e.getMessage()));
        }

        // 2. 推断文件的 Content-Type (MIME Type)
        // 这个方法是私有的，根据文件名后缀判断 MIME 类型
        String inferredContentType = inferContentType(originalFilename);
        MediaType fileMediaType = MediaType.parseMediaType(inferredContentType);
        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("data", bytes)
                .filename(originalFilename)
                .contentType(fileMediaType);

        // 4. 发送 WebClient POST 请求
        return webClient.post()
                .uri(POST_URL)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("图片上传成功，响应: {}", response.substring(0, Math.min(200, response.length())) + "..."))
                .onErrorResume(e -> {
                    log.error("图片上传失败: {}", e.getMessage(), e);
                    return Mono.error(new RuntimeException("上传图片失败: " + e.getMessage()));
                })
                .log();
    }

    /**
     * 根据文件名推断文件的 MIME 类型。
     */
    private String inferContentType(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "application/octet-stream"; // 默认通用二进制类型
        }
        String lowerCaseFileName = fileName.toLowerCase();
        if (lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerCaseFileName.endsWith(".png")) {
            return "image/png";
        } else if (lowerCaseFileName.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerCaseFileName.endsWith(".bmp")) {
            return "image/bmp";
        } else if (lowerCaseFileName.endsWith(".webp")) {
            return "image/webp";
        }
        return "application/octet-stream"; // 未识别类型时，使用通用二进制流
    }

}
