package cn.com.msca.service.api.ks.dto.res;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @program: msca-ivp-extends
 * @description: 活体结果-res
 * @author: oiiaioooooiai
 * @create: 2025-06-04 10:20
 **/
@Data
public class FaceResultRes {

    private String status;

    private String fail_reason;

    private JSONObject biz_info;

    private int time_used;

    private JSONObject idcard_info;

    private JSONObject liveness_result;

    private JSONObject verify_result;

    private JSONObject images;

    private String multifaces_tag;

    private String multifaces_image;
}
