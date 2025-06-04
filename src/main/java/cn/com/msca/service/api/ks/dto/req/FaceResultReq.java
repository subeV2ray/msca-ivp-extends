package cn.com.msca.service.api.ks.dto.req;

import lombok.Data;

/**
 * @program: msca-ivp-extends
 * @description: 活体结果-req
 * @author: oiiaioooooiai
 * @create: 2025-06-04 10:17
 **/
@Data
public class FaceResultReq {
    // 调用此API的api_key
    private String api_key;
    // 调用此API的api_key的secret
    private String api_secret;
    // 通过get_token, notify_url或者return_url返回的活体业务编号
    private String biz_id;
    /**
     * 该参数用于控制验证发生时间信息的返回
     * 0（默认）：不返回做验证发生的时间
     * 1：需要返回验证发生的时间
     */
    private String return_verify_time;

    /**
     * 此参数为可选参数，可在下面三种参数中选择，决定了是否返回活体图像数据：
     * 0（默认）：不需要图像
     * 1：需要返回最佳活体质量图(image_best，当procedure_type为"video"，"still"，"flash"，"meglive_flas"或"distance"时有效）
     * 2：需要返回身份证人像面图像
     * 3：需要返回身份证国徽面图像
     * 4：需要返回所有图像
     * 5：需要返回正脸自拍照片（仅当procedure_type为selfie时有效）
     * 6：需要返回侧脸自拍照片（仅当procedure_type为selfie时有效）
     */
    private String return_image;
}
