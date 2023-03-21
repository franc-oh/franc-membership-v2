package com.franc.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.franc.code.Code;
import com.franc.dto.MyMbspJoinDTO;
import com.franc.dto.MyMbspWithdrawalDTO;
import com.franc.exception.ControllerExceptionHandler;
import com.franc.exception.ExceptionResult;
import com.franc.service.AcntService;
import com.franc.service.MyMbspService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=test"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MyMbspControllerTests {

    @Autowired
    private MyMbspController myMbspController;

    @Autowired
    private MyMbspService myMbspService;

    @Autowired
    private AcntService acntService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(myMbspController)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    private static final String URL = "/api/mbsp/my_mbsp";
    private static final Long ACNT_ID = 5L;
    private static final String MBSP_ID = "M230227000001";

    @Test
    @DisplayName("멤버십가입_실패_유효성검증")
    @Transactional
    public void test_join_fail_valid() throws Exception {
        // #1. Given
        MyMbspJoinDTO.Request requestDTO = MyMbspJoinDTO.Request.builder().build();

        // #2. When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
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
    @DisplayName("멤버십가입_실패_가입오류")
    @Transactional
    public void test_join_fail() throws Exception {
        // #1. Given
        MyMbspJoinDTO.Request requestDTO = MyMbspJoinDTO.Request.builder()
                .acntId(ACNT_ID)
                .mbspId("2")
                .build();

        // #2. When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(objectMapper.writeValueAsString(requestDTO))
        ).andDo(print());

        // #3. Then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("resultCode").value(ExceptionResult.NOT_FOUND_MBSP.getCode().value()))
                .andExpect(jsonPath("resultMessage").value(ExceptionResult.NOT_FOUND_MBSP.getMessage()));
    }

    @Test
    @DisplayName("멤버십가입_성공")
    @Transactional
    public void test_join_success() throws Exception {
        MyMbspJoinDTO.Request requestDTO = MyMbspJoinDTO.Request.builder()
                .acntId(ACNT_ID)
                .mbspId(MBSP_ID)
                .build();

        // #2. When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(objectMapper.writeValueAsString(requestDTO))
        ).andDo(print());

        // #3. Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("resultCode").value(Code.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("resultMessage").value(Code.RESPONSE_MESSAGE_SUCCESS));

    }

    @Test
    @DisplayName("멤버십탈퇴_실패_유효성검증")
    @Transactional
    public void test_withdrawal_fail_valid() throws Exception {
        // #1. Given
        MyMbspWithdrawalDTO.Request request = MyMbspWithdrawalDTO.Request.builder()
                .acntId(ACNT_ID)
                .build();

        // #2. When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(URL)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(print());

        // #3. Then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("resultCode").value(ExceptionResult.PARAMETER_NOT_VALID.getCode().value()))
                .andExpect(jsonPath("resultMessage").value(ExceptionResult.PARAMETER_NOT_VALID.getMessage()));
    }

    @Test
    @DisplayName("멤버십탈퇴_실패_사용자검증실패")
    @Transactional
    public void test_withdrawal_fail_user_check() throws Exception {
        // #1. Given
        MyMbspWithdrawalDTO.Request request = MyMbspWithdrawalDTO.Request.builder()
                .acntId(3L)
                .mbspId(MBSP_ID)
                .build();

        // #2. When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(URL)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(print());

        // #3. Then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("resultCode").value(ExceptionResult.NOT_ACTIVE_ACNT.getCode().value()))
                .andExpect(jsonPath("resultMessage").value(ExceptionResult.NOT_ACTIVE_ACNT.getMessage()));
    }

    @Test
    @DisplayName("멤버십탈퇴_실패_이미탈퇴")
    @Transactional
    public void test_withdrawal_fail_already() throws Exception {
        // #1. Given
        MyMbspWithdrawalDTO.Request request = MyMbspWithdrawalDTO.Request.builder()
                .acntId(ACNT_ID)
                .mbspId(MBSP_ID)
                .build();

        Map<String, Object> paramMap = objectMapper.convertValue(request, Map.class);
        myMbspService.join(paramMap);
        myMbspService.withdrawal(paramMap);

        // #2. When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(URL)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(print());

        // #3. Then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("resultCode").value(ExceptionResult.ALREADY_WITHDRAWAL_MBSP.getCode().value()))
                .andExpect(jsonPath("resultMessage").value(ExceptionResult.ALREADY_WITHDRAWAL_MBSP.getMessage()));
    }

    @Test
    @DisplayName("멤버십탈퇴_성공")
    @Transactional
    public void test_withdrawal_success() throws Exception {
        // #1. Given
        MyMbspWithdrawalDTO.Request request = MyMbspWithdrawalDTO.Request.builder()
                .acntId(ACNT_ID)
                .mbspId(MBSP_ID)
                .build();

        Map<String, Object> paramMap = objectMapper.convertValue(request, Map.class);
        myMbspService.join(paramMap);

        // #2. When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(URL)
                        .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .accept(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                        .content(objectMapper.writeValueAsString(request))
        ).andDo(print());

        // #3. Then
        resultActions.andExpect(status().isAccepted())
                .andExpect(jsonPath("resultCode").value(Code.RESPONSE_CODE_SUCCESS))
                .andExpect(jsonPath("resultMessage").value(Code.RESPONSE_MESSAGE_SUCCESS));
    }
}
