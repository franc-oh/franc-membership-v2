package com.franc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.franc.code.Code;
import com.franc.dto.MbspFindAllDTO;
import com.franc.dto.MyMbspJoinDTO;
import com.franc.dto.MyMbspWithdrawalDTO;
import com.franc.service.AcntService;
import com.franc.service.MbspService;
import com.franc.service.MyMbspService;
import com.franc.vo.MbspAndMyMbspVo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

        // #1. 멤버십리스트 가져오기
        Map<String, Object> paramMap = objectMapper.convertValue(request, Map.class);
        List<MbspAndMyMbspVo> procList = mbspService.findAllAndMyMbspYn(paramMap);

        // #2. 응답처리
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

}
