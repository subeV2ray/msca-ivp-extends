package cn.com.msca.service.api.ks.dto;

import lombok.Data;

/**
 * @program: msca-ivp-extends
 * @description: 获取token结果
 * @author: oiiaioooooiai
 * @create: 2025-06-03 01:57
 **/
@Data
public class TokenRes {
    // 用于区分每一次请求的唯一的字符串，此字符串可以用于后续数据反查，此字段必定返回
    private String request_id;
    // 整个请求所花费的时间，单位为毫秒，此字段必定返回
    private int time_used;
    // 一个字符串，可用于DoVerification接口，调用DoVerification时传入此参数，即可按照上述配置进行活体检测（注：每个token只能被使用一次）
    private String token;
    // 业务流串号，可以用于反查比对结果
    private String biz_id;
    // 一个时间戳，表示token的有效期
    private int expired_time;
    // 当请求失败时才会返回此字符串，具体返回内容见后续错误信息章节，否则此字段不存在
    private String error_message;
}
