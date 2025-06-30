package cn.com.msca.service.api.dts.dto.token;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: msca-personal-verify
 * @description: token-请求对象
 * @author: oiiaioooooiai
 * @create: 2025-06-30 14:19
 **/
@Data
@AllArgsConstructor
public class TokenReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 业务系统id
     */
    private String appId;

    /**
     * 业务系统安全码
     */
    private String appSecret;
}
