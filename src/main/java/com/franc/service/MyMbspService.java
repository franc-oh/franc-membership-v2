package com.franc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franc.code.Status;
import com.franc.exception.BizException;
import com.franc.exception.ExceptionResult;
import com.franc.mapper.MyMbspMapper;
import com.franc.util.DateUtil;
import com.franc.vo.MyMbspVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyMbspService {
    private static final Logger logger = LoggerFactory.getLogger(MyMbspService.class);

    private final MyMbspMapper myMbspMapper;

    private final AcntService acntService;

    private final MbspService mbspService;

    private final ObjectMapper objectMapper;

    /**
     * 멤버십 가입
     * @param paramMap
     * @throws Exception
     */
    public void join(Map<String, Object> paramMap) throws Exception {
        boolean withdrawal = false;

        logger.info("MyMbsp join Start : " + paramMap.toString());

        // #1-1. 필수 값 체크
        if(paramMap.isEmpty())
            throw new BizException(ExceptionResult.PARAMETER_NOT_VALID);

        Long acntId = (Long) paramMap.get("acntId");
        String mbspId = (String) paramMap.get("mbspId");

        if(acntId == null)
            throw new BizException(ExceptionResult.PARAMETER_NOT_VALID);
        if(mbspId == null)
            throw new BizException(ExceptionResult.PARAMETER_NOT_VALID);

        // #1-2. 사용자 체크
        acntService.findByIdAndCheckActive(acntId);

        // #1-3. 멤버십 체크
        mbspService.findByIdAndCheckActive(mbspId);


        // #2. 해당정보로 가입된 멤버십 정보 가져오기
        MyMbspVO checkVO = myMbspMapper.findById(paramMap);
        if(checkVO != null) {
            if(checkVO.getStatus() == Status.USE.getCode()) {
                throw new BizException(ExceptionResult.ALREADY_JOIN_MY_MBSP);

            } else if(checkVO.getStatus() == Status.WITHDRAWAL.getCode()) {
                // '탈퇴'상태일 경우 탈퇴 후 1일 지나야 재가입 가능
                if(DateUtil.compareNow(checkVO.getWithdrawalDate()) >= 0) {
                    throw new BizException(ExceptionResult.REJOIN_NOT_YET);
                }

                withdrawal = true;
            }
        }

        // #2. 등록 or 재가입
        MyMbspVO procVO = objectMapper.convertValue(paramMap, MyMbspVO.class);

        if(withdrawal) { // 재가입
            procVO.rejoin();
            myMbspMapper.modify(procVO);

        } else { // 신규가입 (바코드 생성)
            procVO.setBarCd(createBarCd());
            myMbspMapper.save(procVO);
        }

        logger.info("MyMbsp join Success : " + procVO.toString());

    }

    /**
     * 멤버십 탈퇴
     * @param paramMap
     * @throws Exception
     */
    public void withdrawal(Map<String, Object> paramMap) throws Exception {
        logger.info("MyMbsp withdrawal Start : " + paramMap.toString());

        // #1. 필수 값 체크
        if(paramMap.isEmpty())
            throw new BizException(ExceptionResult.PARAMETER_NOT_VALID);

        Long acntId = (Long) paramMap.get("acntId");
        String mbspId = (String) paramMap.get("mbspId");

        if(acntId == null)
            throw new BizException(ExceptionResult.PARAMETER_NOT_VALID);
        if(mbspId == null)
            throw new BizException(ExceptionResult.PARAMETER_NOT_VALID);

        // #2. 멤버십 가입내역 가져오기
        MyMbspVO procVO = myMbspMapper.findById(paramMap);
        if(procVO == null) {
            throw new BizException(ExceptionResult.NOT_JOIN_MBSP);
        }

        // 이미 탈퇴한 멤버십이면 에러
        if(Status.WITHDRAWAL.getCode() == procVO.getStatus()
                || procVO.getWithdrawalDate() != null) {
            throw new BizException(ExceptionResult.ALREADY_WITHDRAWAL_MBSP);
        }

        if(procVO.withdrawal())
            myMbspMapper.modify(procVO);

        logger.info("MyMbsp withdrawal Success : " + procVO.toString());

    }

    /**
     * 바코드 생성
     * @return
     * @throws Exception
     */
    public String createBarCd() throws Exception {
        Integer seq = myMbspMapper.getBarCdSeq();
        if(seq == null) {
            throw new BizException(ExceptionResult.CREATE_BARCODE_FAIL);
        }

        StringBuilder barcode = new StringBuilder(DateUtil.nowDateToString());
        barcode.append(String.format("%06d", seq));

        logger.debug("createBarCd => " + barcode.toString());
        return barcode.toString();
    }


}
