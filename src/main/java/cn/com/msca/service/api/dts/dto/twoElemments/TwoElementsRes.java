package cn.com.msca.service.api.dts.dto.twoElemments;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: msca-personal-verify
 * @description: 二要素-响应参数
 * @author: oiiaioooooiai
 * @create: 2025-06-30 14:38
 **/
@Data
public class TwoElementsRes implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    private String code;

    private String msg;

    private TwoElementsData data;

    @Data
    private static class TwoElementsData {

        /**
         * 核验事件编号, 用户获取证书及核验报告
         */
        private String eventId;

        /**
         * 核验结果
         */
        private String result;
    }
}
