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
public class ThreeElementRes implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String code;

    private String msg;

    private ThreeElementData data;

    @Data
    private static class ThreeElementData {

        /**
         * 核验事件编号，用于获取证书及核验报告
         */
        private String eventId;

        /**
         * 核验结果
         */
        private String result;

        /**
         * 照片相似度
         */
        private String rxfs;
    }
}
