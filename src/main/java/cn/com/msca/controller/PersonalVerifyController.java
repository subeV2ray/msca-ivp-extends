package cn.com.msca.controller;

import cn.com.msca.service.api.dts.dto.threeElements.ThreeElementReq;
import cn.com.msca.service.api.dts.dto.threeElements.ThreeElementRes;
import cn.com.msca.service.api.dts.dto.twoElemments.TwoElementsReq;
import cn.com.msca.service.api.dts.dto.twoElemments.TwoElementsRes;
import cn.com.msca.service.bus.PersonalVerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @program: msca-ivp-extends
 * @description: 个人核验-controller
 * @author: oiiaioooooiai
 * @create: 2025-06-30 15:57
 **/
@RestController
@RequestMapping("/personal/verify")
@RequiredArgsConstructor
public class PersonalVerifyController {

    private final PersonalVerifyService personalVerifyService;


    /**
     * 个人二要素
     * @param twoElementsReq
     * @return
     */
    @PostMapping("/twoElement")
    public Mono<TwoElementsRes> twoElement(@RequestBody TwoElementsReq twoElementsReq) {
        return personalVerifyService.twoElement(twoElementsReq);
    }

    /**
     * 个人三要素
     * @param threeElementReq
     * @return
     */
    @PostMapping("/threeElement")
    public Mono<ThreeElementRes> threeElement(@RequestBody ThreeElementReq threeElementReq) {
        return personalVerifyService.threeElement(threeElementReq);
    }
}
