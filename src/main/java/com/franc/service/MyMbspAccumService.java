package com.franc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franc.code.Status;
import com.franc.exception.BizException;
import com.franc.exception.ExceptionResult;
import com.franc.mapper.MyMbspAccumMapper;
import com.franc.util.DateUtil;
import com.franc.util.NumberUtil;
import com.franc.vo.AcntVO;
import com.franc.vo.MyMbspAccumVO;
import com.franc.vo.MyMbspDetailInfoVo;
import com.franc.vo.MyMbspVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyMbspAccumService {
    private static final Logger logger = LoggerFactory.getLogger(MyMbspAccumService.class);

    private final MyMbspAccumMapper myMbspAccumMapper;

    private final MyMbspService myMbspService;

    private final AcntService acntService;

    private final ObjectMapper objectMapper;


    /**
     * 멤버십 바코드 적립
     * @param paramMap {barCd, frchId, tradeAmt}
     * @return
     * @throws Exception
     */
    public String accumToBarCd(Map<String, Object> paramMap) throws Exception {
        String cancelBarCd = "";
        logger.info("MyMbspAccum AccumToBarCd Start : " + paramMap.toString());

        // #1. 필수 값 체크
        if(paramMap.isEmpty() || !paramMap.containsKey("barCd")
                || !paramMap.containsKey("frchId") || !paramMap.containsKey("tradeAmt")) {

            throw new BizException(ExceptionResult.PARAMETER_NOT_VALID);
        }

        String barCd = (String) paramMap.get("barCd");
        String frchId = (String) paramMap.get("frchId");
        Integer tradeAmt = (Integer) paramMap.get("tradeAmt");

        if(!StringUtils.hasText(barCd) || !StringUtils.hasText(frchId)
                || tradeAmt == null) {

            throw new BizException(ExceptionResult.PARAMETER_NOT_VALID);
        }

        // #2. 바코드로 멤버십 상세정보 가져오기
        Map<String, Object> myMbspDetailInfoParam = new HashMap<>();
        myMbspDetailInfoParam.put("barCd", barCd);
        MyMbspDetailInfoVo myMbspDetailInfo = myMbspService.findDetailByBarCdAndFrchId(myMbspDetailInfoParam);
        if(myMbspDetailInfo == null) {
            throw new BizException(ExceptionResult.NOT_FOUND_MBSP_TO_BARCODE);
        }

        Long acntId = myMbspDetailInfo.getAcntId();
        String mbspId = myMbspDetailInfo.getMbspId();
        String mbspGrdCd = myMbspDetailInfo.getMbspGrdCd();
        int accumRat = myMbspDetailInfo.getMbspGrdInfo().getAccumRat();
        int activeMonths = myMbspDetailInfo.getMbspInfo().getActiveMonths();

        // #2-1. 멤버십상태 체크
        if(myMbspDetailInfo.getMbspInfo().getStatus() != Status.USE.getCode()) {
            throw new BizException(ExceptionResult.NOT_ACTIVE_MBSP);
        } else if(myMbspDetailInfo.getFrchInfo().getStatus() != Status.USE.getCode()) {
            throw new BizException(ExceptionResult.NOT_ACTIVE_FRANCHISEE);
        }

        // #2-2. 회원상태 체크
        acntService.findByIdAndCheckActive(acntId);


        // #3. 적립내역 저장
        cancelBarCd = myMbspService.createBarCd();
        MyMbspAccumVO myMbspAccumVO = MyMbspAccumVO.builder()
                .cancelBarCd(cancelBarCd)
                .acntId(acntId)
                .mbspId(mbspId)
                .frchId(frchId)
                .tradeAmt(tradeAmt)
                .mbspGrdCd(mbspGrdCd)
                .accumRat(accumRat)
                .accumPoint(NumberUtil.getCalcPerAmt(tradeAmt, accumRat))
                .expireYmd(DateUtil.getAddMonth(activeMonths))
                .build();

        myMbspAccumMapper.save(myMbspAccumVO);



        return cancelBarCd;
    }

}
