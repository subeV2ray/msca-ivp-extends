package cn.com.msca.service.api.dts.dto.threeElements;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: msca-ivp-extends
 * @description:
 * @author: oiiaioooooiai
 * @create: 2025-06-30 17:03
 **/

@Data
public class ThreeElementReq implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户身份号码
     */
    private String idNum;

    /**
     * 用户姓名
     */
    private String fullName;

    /**
     * BASE64编码的人像图
     * 片；建议原始尺寸为
     * 10~20KB，否则可能
     * 会因为过小而导致人
     * 像比对不通；
     */
    private String portrait;

    /**
     * P10证书请求文件内容
     */
    private String p10;

    /**
     * 待签原文Hash，用户待
     * 签署授权书的哈希值
     */
    private String dataToSign;
}
