package com.franc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.franc.code.Code;
import com.franc.dto.MbspFindAllDTO;
import com.franc.dto.MbspFindByIdDTO;
import com.franc.exception.BizException;
import com.franc.exception.ExceptionResult;
import com.franc.service.AcntService;
import com.franc.service.MbspService;
import com.franc.vo.MbspAndMyMbspVo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mbsp/mbsp")
@RequiredArgsConstructor
public class MbspController {
    private static final Logger logger = LoggerFactory.getLogger(MbspController.class);

    private final MbspService mbspService;

    private final AcntService acntService;

    private final ObjectMapper objectMapper;

    @GetMapping("/all")
    public ResponseEntity<?> findAll(@RequestBody @Valid MbspFindAllDTO.Request request) throws Exception {
        MbspFindAllDTO.Response response = new MbspFindAllDTO.Response();
        logger.info("멤버십리스트조회_Request => {}", request.toString());

        // #1. 회원 유효성 체크
        Long acntId = request.getAcntId();
        if(acntId != null)
            acntService.findByIdAndCheckActive(acntId);

        // #2. 멤버십리스트 가져오기
        Map<String, Object> paramMap = objectMapper.convertValue(request, Map.class);
        List<MbspAndMyMbspVo> procList = mbspService.findAllAndMyMbspYn(paramMap);

        // #3. 응답처리
        response.setResultCode(Code.RESPONSE_CODE_SUCCESS);
        response.setResultMessage(Code.RESPONSE_MESSAGE_SUCCESS);

        if(!procList.isEmpty()) {
            response.setMbspCnt(procList.size());
            response.setMbspList(objectMapper.convertValue(procList,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, MbspFindAllDTO.MbspInfo.class)
            ));
        }

        logger.info("멤버십리스트조회_Response => {}", response.toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<?> findById(@RequestBody @Valid MbspFindByIdDTO.Request request) throws Exception {
        MbspFindByIdDTO.Response response = new MbspFindByIdDTO.Response();
        logger.info("멤버십상세조회_Request => {}", request.toString());

        // #1. 회원 유효성 체크
        Long acntId = request.getAcntId();
        if(acntId != null) {
            acntService.findByIdAndCheckActive(acntId);
        }

        // #2. 멤버십상세 가져오기
        Map<String, Object> paramMap = objectMapper.convertValue(request, Map.class);
        MbspAndMyMbspVo procVo = mbspService.findByIdAndMyMbspInfo(paramMap);
        if(procVo == null) {
            throw new BizException(ExceptionResult.NOT_FOUND_MBSP);
        }

        // #3. 응답처리
        response = objectMapper.convertValue(procVo, MbspFindByIdDTO.Response.class);

        MbspAndMyMbspVo.MyMbspInfo myMbspInfo = procVo.getMyMbspInfo();
        if(myMbspInfo != null) {
            response.setMyMbspInfo(objectMapper.convertValue(myMbspInfo, MbspFindByIdDTO.MyMbspInfo.class));
        }

        response.setResultCode(Code.RESPONSE_CODE_SUCCESS);
        response.setResultMessage(Code.RESPONSE_MESSAGE_SUCCESS);


        logger.info("멤버십상세조회_Response => {}", response.toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
