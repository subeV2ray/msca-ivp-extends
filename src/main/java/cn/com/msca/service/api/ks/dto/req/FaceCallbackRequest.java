package cn.com.msca.service.api.ks.dto.req;

import lombok.Data;

@Data
public class FaceCallbackRequest {
    private String data;
    private String sign;
}