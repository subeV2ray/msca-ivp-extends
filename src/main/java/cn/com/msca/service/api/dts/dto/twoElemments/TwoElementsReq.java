package cn.com.msca.service.api.dts.dto.twoElemments;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: msca-personal-verify
 * @description: 二要素-请求对象
 * @author: oiiaioooooiai
 * @create: 2025-06-30 14:32
 **/
@Data
public class TwoElementsReq implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * 用户身份号码
     */
    private String idNum;

    /**
     * 用户姓名
     */
    private String fullName;

    /**
     * P10证书请求文件内容
     */
    private String p10;

    /**
     * 待签原文Hash, 用户待签署授权书的哈希值
     */
    private String dataToSign;
}
