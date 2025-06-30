package cn.com.msca.service.api.dts.dto.token;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: msca-personal-verify
 * @description: token-响应对象
 * @author: oiiaioooooiai
 * @create: 2025-06-30 14:28
 **/
@Data
public class TokenRes implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * 操作结果代码
     */
    private String code;

    /**
     * 返回信息代码
     */
    private String msg;

    private TokenData data;

    @Data
    public static class TokenData {
        /**
         * 业务调用令牌
         */
        private String token;

        /**
         * 令牌时效(秒)
         */
        private Integer expireSeconds;
    }
}
