package com.franc.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.franc.code.Code;
import com.franc.dto.MbspFindAllDTO;
import com.franc.exception.ControllerExceptionHandler;
import com.franc.exception.ExceptionResult;
import com.franc.mapper.MyMbspMapper;
import com.franc.service.MyMbspService;
import com.franc.vo.MyMbspVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=test"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MbspControllerTests {

    @Autowired
    private MbspController mbspController;

    @Autowired
    private MyMbspMapper myMbspMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private static final String URL = "/api/mbsp/mbsp";
    private static final Long ACNT_ID = 5L;
    private static final String MBSP_ID = "M230227000001";

    private static final String BAR_CD = "1234556";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(mbspController)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    @DisplayName("멤버십리스트조회_실패_유효성검증실패")
    @Transactional
    public void test_findAll_fail_not_valid() throws Exception {
        // #1. Given
        String url = URL + "/all";
        MbspFindAllDTO.Request requestDTO = MbspFindAllDTO.Request.builder()
                .acntId(0L)
                .pageNo(-1)
                .pageLimit(0)
                .build();

        // #2. When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(objectMapper.writeValueAsString(requestDTO))
        ).andDo(print());

        // #3. Then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("resultCode").value(ExceptionResult.PARAMETER_NOT_VALID.getCode().value()))
                .andExpect(jsonPath("resultMessage").value(ExceptionResult.PARAMETER_NOT_VALID.getMessage()));


    }

    @Test
    @DisplayName("멤버십리스트조회_성공_회원ID_NULL_모든멤버십_가입여부N으로_조회되야함")
    @Transactional
    public void test_findAll_success_acntId_null() throws Exception {
        // #1. Given
        String url = URL + "/all";
        MbspFindAllDTO.Request requestDTO = MbspFindAllDTO.Request.builder().build();

        // #2. When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(objectMapper.writeValueAsString(requestDTO))
        ).andDo(print());

        // #3. Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("resultCode").value(Code.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("resultMessage").value(Code.RESPONSE_MESSAGE_SUCCESS))
                .andExpect(jsonPath("mbspCnt", is(greaterThan(0))))
                .andExpect(jsonPath("$.mbspList[0].myMbspYn", is("N")));
    }

    @Test
    @DisplayName("멤버십리스트조회_성공_가입멤버십_가입여부Y로_조회되야함")
    @Transactional
    public void test_findAll_success_myMbspYn() throws Exception {
        // #1. Given
        String url = URL + "/all";
        MbspFindAllDTO.Request requestDTO = MbspFindAllDTO.Request.builder()
                .acntId(ACNT_ID)
                .build();

        myMbspMapper.save(MyMbspVO.builder()
                .acntId(ACNT_ID)
                .mbspId(MBSP_ID)
                .barCd(BAR_CD)
                .build());

        // #2. When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(objectMapper.writeValueAsString(requestDTO))
        ).andDo(print());

        // #3. Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("resultCode").value(Code.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("resultMessage").value(Code.RESPONSE_MESSAGE_SUCCESS))
                .andExpect(jsonPath("mbspCnt", is(greaterThan(0))))
                .andExpect(jsonPath("$.mbspList[0].mbspId", is(MBSP_ID)))
                .andExpect(jsonPath("$.mbspList[0].myMbspYn", is("Y")));
    }


}
