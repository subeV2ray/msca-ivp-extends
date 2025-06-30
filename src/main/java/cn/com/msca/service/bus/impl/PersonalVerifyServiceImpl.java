package cn.com.msca.service.bus.impl;

import cn.com.msca.service.api.dts.DtsAPI;
import cn.com.msca.service.api.dts.dto.threeElements.ThreeElementReq;
import cn.com.msca.service.api.dts.dto.threeElements.ThreeElementRes;
import cn.com.msca.service.api.dts.dto.token.TokenReq;
import cn.com.msca.service.api.dts.dto.token.TokenRes;
import cn.com.msca.service.api.dts.dto.twoElemments.TwoElementsReq;
import cn.com.msca.service.api.dts.dto.twoElemments.TwoElementsRes;
import cn.com.msca.service.bus.PersonalVerifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @program: msca-ivp-extends
 * @description: 个人核验-serviceImpl
 * @author: oiiaioooooiai
 * @create: 2025-06-30 16:07
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalVerifyServiceImpl implements PersonalVerifyService {

    private final DtsAPI dtsAPI;


    /**
     * 个人二要素
     * @param twoElementsReq
     * @return
     */
    @Override
    public Mono<TwoElementsRes> twoElement(TwoElementsReq twoElementsReq) {
        return dtsAPI.twoElements(twoElementsReq);
    }


    /**
     * 个人三要素
     * @param threeElementReq
     * @return
     */
    @Override
    public Mono<ThreeElementRes> threeElement(ThreeElementReq threeElementReq) {
        return dtsAPI.threeElements(threeElementReq);
    }
}
