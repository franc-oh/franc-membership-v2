package com.franc.service;

import com.franc.code.Status;
import com.franc.exception.BizException;
import com.franc.exception.ExceptionResult;
import com.franc.mapper.AcntMapper;
import com.franc.vo.AcntVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AcntService {
    private static final Logger logger = LoggerFactory.getLogger(AcntService.class);

    private final AcntMapper acntMapper;

    /**
     * 회원정보가져오기 및 계정상태 체크
     * @param acntId
     * @return
     * @throws Exception
     */
    public AcntVO findByIdAndCheckActive(Long acntId) throws Exception {
        AcntVO resultVO = acntMapper.findById(acntId);
        if(resultVO == null)
            throw new BizException(ExceptionResult.NOT_FOUND_ACNT);
        else if(resultVO.getStatus() != Status.USE.getCode())
            throw new BizException(ExceptionResult.NOT_ACTIVE_ACNT);

        return resultVO;
    }

}
