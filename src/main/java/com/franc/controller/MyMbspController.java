package com.franc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franc.code.Code;
import com.franc.dto.MyMbspJoinDTO;
import com.franc.service.AcntService;
import com.franc.service.MyMbspService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/mbsp/my_mbsp")
@RequiredArgsConstructor
public class MyMbspController {
    private static final Logger logger = LoggerFactory.getLogger(MyMbspController.class);

    private final MyMbspService myMbspService;

    private final AcntService acntService;

    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<?> join(@RequestBody @Valid MyMbspJoinDTO.Request request) throws Exception {
        MyMbspJoinDTO.Response response = new MyMbspJoinDTO.Response();

        logger.info("멤버십가입_Request => {}", request.toString());

        // #1. 가입처리 (회원 및 멤버십 유효체크 모두 안에서 진행됨)
        Map<String, Object> paramMap = objectMapper.convertValue(request, Map.class);
        myMbspService.join(paramMap);

        // #2. 응답처리
        response.setResultCode(Code.RESPONSE_CODE_SUCCESS);
        response.setResultMessage(Code.RESPONSE_MESSAGE_SUCCESS);

        logger.info("멤버십가입_Response => {}", response.toString());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
