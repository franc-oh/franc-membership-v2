package com.franc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franc.code.Status;
import com.franc.exception.BizException;
import com.franc.exception.ExceptionResult;
import com.franc.mapper.MbspMapper;
import com.franc.util.PageUtil;
import com.franc.vo.MbspAndMyMbspVo;
import com.franc.vo.MbspVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MbspService {
    private static final Logger logger = LoggerFactory.getLogger(MbspService.class);

    private final MbspMapper mbspMapper;

    private final ObjectMapper objectMapper;

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


    /**
     * 멤버십리스트 및 가입여부 가져오기
     * @param paramMap {acntId, pageNo, pageLimit}
     * @return
     * @throws Exception
     */
    public List<MbspAndMyMbspVo> findAllAndMyMbspYn(Map<String, Object> paramMap) throws Exception {
        Integer pageNo = null;
        Integer pageLimit = null;
        Long acntId = null;

        if(!paramMap.isEmpty()) {
            if(paramMap.get("pageNo") != null) pageNo = (Integer) paramMap.get("pageNo");
            if(paramMap.get("pageLimit") != null) pageLimit = (Integer) paramMap.get("pageLimit");
            if(paramMap.get("acntId") != null) acntId = (Long) paramMap.get("acntId");
        }

        Map<String, Object> procMap = PageUtil.getPageMap(pageNo, pageLimit);
        procMap.put("acntId", acntId);

        return mbspMapper.findAllAndMyMbspYn(procMap);
    }


    /**
     * 멤버십정보 및 가입정보 가져오기
     * @param paramMap {acntId, mbspId}
     * @return result
     * @throws Exception
     */
    public MbspAndMyMbspVo findByIdAndMyMbspInfo(Map<String, Object> paramMap) throws Exception {
        MbspAndMyMbspVo result = null;

        if(!paramMap.isEmpty() && paramMap.containsKey("acntId")
                && paramMap.containsKey("mbspId")) {

            result = mbspMapper.findByIdAndMyMbspInfo(paramMap);
        }

        return result;

    }
}
