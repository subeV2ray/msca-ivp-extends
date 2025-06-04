package cn.com.msca.service.api.ks.dto.req;

import lombok.Data;

/**
 * @program: msca-ivp-extends
 * @description: 旷视-dto
 * @author: oiiaioooooiai
 * @create: 2025-06-03 00:47
 **/
@Data
public class TokenReq {
    // 调用此API的api_key
    private String api_key;
    // 调用此API的api_key
    private String api_secret;

    // 用户完成或取消验证后网页跳转的目标URL（回调方法为Post）
    private String return_url;
    /**
     * 用户完成验证、取消验证、或验证超时后，
     * 由FaceID服务器请求客户服务器的URL（推荐为HTTPS页面，如果为HTTP则用户需要通过签名自行校验数据可信性，回调方法为Post）
     * 注：出于安全性考虑，FaceID验证服务服务对服务器端回调端口有白名单要求，支持的端口有：443，5000，16003，8883，8028
     */
    private String notify_url;

    // 客户业务流水号，该号必须唯一。并会在notify和return时原封不动的返回给您的服务器，以帮助您确认每一笔业务的归属。此字段不超过128字节
    private String biz_no;

    private String comparison_type;

    private String uuid;
}
