package cn.com.msca.service.bus;

import cn.com.msca.service.api.dts.dto.threeElements.ThreeElementReq;
import cn.com.msca.service.api.dts.dto.threeElements.ThreeElementRes;
import cn.com.msca.service.api.dts.dto.token.TokenReq;
import cn.com.msca.service.api.dts.dto.token.TokenRes;
import cn.com.msca.service.api.dts.dto.twoElemments.TwoElementsReq;
import cn.com.msca.service.api.dts.dto.twoElemments.TwoElementsRes;
import reactor.core.publisher.Mono;

/**
 * @program: msca-ivp-extends
 * @description: 个人核验-service
 * @author: oiiaioooooiai
 * @create: 2025-06-30 16:06
 **/
public interface PersonalVerifyService {

    Mono<TwoElementsRes> twoElement(TwoElementsReq twoElementsReq);

    Mono<ThreeElementRes> threeElement(ThreeElementReq threeElementReq);
}
