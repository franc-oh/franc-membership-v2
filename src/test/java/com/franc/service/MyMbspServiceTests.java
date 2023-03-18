package com.franc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franc.code.Status;
import com.franc.exception.BizException;
import com.franc.exception.ExceptionResult;
import com.franc.mapper.AcntMapper;
import com.franc.mapper.MyMbspMapper;
import com.franc.vo.AcntVO;
import com.franc.vo.MbspVO;
import com.franc.vo.MyMbspVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyMbspServiceTests {

    @InjectMocks
    private MyMbspService myMbspService;

    @Mock
    private MyMbspMapper myMbspMapper;

    @Mock
    private AcntService acntService;

    @Mock
    private MbspService mbspService;

    @Spy
    private ObjectMapper objectMapper;


    private static final Long ACNT_ID = 5L;
    private static final String MBSP_ID = "M230227000001";
    private static final String BAR_CD = "1234567";


    @Test
    @DisplayName("멤버십가입_실패_필수값")
    public void test_join_fail_valid() throws Exception {
        // #1. Given
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("acntId", ACNT_ID);
        //paramMap.put("mbspId", MBSP_ID);

        // #2. When
        BizException exception
                = assertThrows(BizException.class, () -> myMbspService.join(paramMap));

        // #3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.PARAMETER_NOT_VALID);
    }

    @Test
    @DisplayName("멤버십가입_실패_회원체크에러")
    public void test_join_fail_not_found_acnt() throws Exception {
        // #1. Given
        MyMbspVO paramVO = buildVo(new HashMap<>());

        when(acntService.findByIdAndCheckActive(anyLong()))
                .thenThrow(new BizException(ExceptionResult.NOT_FOUND_ACNT));

        Map<String, Object> paramMap = objectMapper.convertValue(paramVO, Map.class);

        // #2. When
        BizException exception
                = assertThrows(BizException.class, () -> myMbspService.join(paramMap));

        // #3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.NOT_FOUND_ACNT);
    }

    @Test
    @DisplayName("멤버십가입_실패_멤버십체크에러")
    public void test_join_fail_not_active_mbsp() throws Exception {
        // #1. Given
        MyMbspVO paramVO = buildVo(new HashMap<>());

        when(acntService.findByIdAndCheckActive(anyLong()))
                .thenReturn(AcntVO.builder().build());

        when(mbspService.findByIdAndCheckActive(anyString()))
                .thenThrow(new BizException(ExceptionResult.NOT_ACTIVE_MBSP));

        Map<String, Object> paramMap = objectMapper.convertValue(paramVO, Map.class);

        // #2. When
        BizException exception
                = assertThrows(BizException.class, () -> myMbspService.join(paramMap));

        // #3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.NOT_ACTIVE_MBSP);
    }

    @Test
    @DisplayName("멤버십가입_실패_중복")
    public void test_join_fail_already() throws Exception {
        // #1. Given
        MyMbspVO paramVO = buildVo(new HashMap<>());

        when(acntService.findByIdAndCheckActive(anyLong()))
                .thenReturn(AcntVO.builder().build());

        when(mbspService.findByIdAndCheckActive(anyString()))
                .thenReturn(MbspVO.builder().build());

        when(myMbspMapper.findById(anyMap()))
                .thenReturn(paramVO);

        Map<String, Object> paramMap = objectMapper.convertValue(paramVO, Map.class);

        // #2. When
        BizException exception
                = assertThrows(BizException.class, () -> myMbspService.join(paramMap));

        // #3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.ALREADY_JOIN_MY_MBSP);
    }

    @Test
    @DisplayName("멤버십가입_성공_신규 + 바코드생성")
    @Transactional
    public void join_success_new() throws Exception {
        // #1. Given
        Map<String, Object> paramMap = buildKeyMap();

        MyMbspVO paramVO = buildVo(paramMap);

        when(acntService.findByIdAndCheckActive(anyLong()))
                .thenReturn(AcntVO.builder().build());

        when(mbspService.findByIdAndCheckActive(anyString()))
                .thenReturn(MbspVO.builder().build());

        when(myMbspMapper.findById(anyMap()))
                .thenReturn(null);

        doNothing().when(myMbspMapper).save(any(MyMbspVO.class));

        // #2. When
        myMbspService.join(paramMap);

        // #3. Then
        verify(myMbspMapper, times(1)).findById(anyMap());
        verify(myMbspMapper, times(1)).save(any(MyMbspVO.class));
    }

    @Test
    @DisplayName("멤버십가입_성공_재가입")
    @Transactional
    public void join_success_rejoin() throws Exception {
        // #1. Given
        Map<String, Object> paramMap = buildKeyMap();
        Map<String, Object> voMap = buildKeyMap();
        voMap.put("status", Status.WITHDRAWAL.getCode());
        voMap.put("withdrawalDate", LocalDateTime.now());
        MyMbspVO vo = buildVo(voMap);

        when(acntService.findByIdAndCheckActive(anyLong()))
                .thenReturn(AcntVO.builder().build());

        when(mbspService.findByIdAndCheckActive(anyString()))
                .thenReturn(MbspVO.builder().build());

        when(myMbspMapper.findById(anyMap()))
                .thenReturn(vo);

        doNothing().when(myMbspMapper).modify(any(MyMbspVO.class));

        // #2. When
        myMbspService.join(paramMap);

        // #3. Then
        verify(myMbspMapper, times(1)).findById(anyMap());
        verify(myMbspMapper, times(1)).modify(any(MyMbspVO.class));
    }



    public Map<String, Object> buildKeyMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("acntId", ACNT_ID);
        map.put("mbspId", MBSP_ID);
        return map;
    }

    public MyMbspVO buildVo(Map<String, Object> paramMap) throws Exception {
        Long acntId = ACNT_ID;
        String mbspId = MBSP_ID;
        String barCd = BAR_CD;
        LocalDateTime withdrawalDate = null;
        Character status = Status.USE.getCode();

        if(!paramMap.isEmpty()) {
            if(paramMap.get("acntId") != null) acntId = (Long) paramMap.get("acntId");
            if(paramMap.get("mbspId") != null) mbspId = (String) paramMap.get("mbspId");
            if(paramMap.get("barCd") != null) barCd = (String) paramMap.get("barCd");
            if(paramMap.get("withdrawalDate") != null) withdrawalDate = (LocalDateTime) paramMap.get("withdrawalDate");
            if(paramMap.get("status") != null) status = (Character) paramMap.get("status");
        }

        return MyMbspVO.builder()
                .acntId(acntId)
                .mbspId(mbspId)
                .barCd(barCd)
                .status(status)
                .withdrawalDate(withdrawalDate)
                .build();
    }

}
