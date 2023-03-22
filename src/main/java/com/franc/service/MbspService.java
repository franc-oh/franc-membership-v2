package com.franc.service;

import com.franc.code.Status;
import com.franc.exception.BizException;
import com.franc.exception.ExceptionResult;
import com.franc.mapper.MbspMapper;
import com.franc.vo.MbspVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MbspService {
    private static final Logger logger = LoggerFactory.getLogger(MbspService.class);

    private final MbspMapper mbspMapper;

    /**
     * 멤버십정보가져오기 및 계정상태 체크
     * @param mbspId
     * @return
     * @throws Exception
     */
    public MbspVO findByIdAndCheckActive(String mbspId) throws Exception {
        MbspVO resultVO = mbspMapper.findById(mbspId);
        if(resultVO == null)
            throw new BizException(ExceptionResult.NOT_FOUND_MBSP);
        else if(resultVO.getStatus() != Status.USE.getCode())
            throw new BizException(ExceptionResult.NOT_ACTIVE_MBSP);

        return resultVO;
    }

}
