package com.franc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.franc.code.Status;
import com.franc.exception.BizException;
import com.franc.exception.ExceptionResult;
import com.franc.mapper.MyMbspAccumMapper;
import com.franc.vo.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyMbspAccumServiceTests {

    @InjectMocks
    private MyMbspAccumService myMbspAccumService;

    @Mock
    private MyMbspAccumMapper myMbspAccumMapper;

    @Mock
    private MyMbspService myMbspService;

    @Mock
    private AcntService acntService;

    @Spy
    private ObjectMapper objectMapper;


    private static final Long ACNT_ID = 5L;
    private static final String MBSP_ID = "M230227000001";

    private static final String FRCH_ID = "F230228000003";
    private static final String BAR_CD = "1234567";


    @Test
    @DisplayName("멤버십바코드적립_실패_필수값")
    @Transactional
    public void test_accumToBarCd_fail_valid() throws Exception {
        // #1. Given
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("barCd", "");
        paramMap.put("frchId", "");
        paramMap.put("tradeAmt", null);

        // #2. When
        BizException exception =
                assertThrows(BizException.class, () -> myMbspAccumService.accumToBarCd(paramMap));

        // #3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.PARAMETER_NOT_VALID);
    }

    @Test
    @DisplayName("멤버십바코드적립_실패_바코드로멤버십못찾음")
    @Transactional
    public void test_accumToBarCd_fail_notFound_mbsp() throws Exception {
        // #1. Given
        int tradeAmt = 10000;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("barCd", BAR_CD);
        paramMap.put("frchId", FRCH_ID);
        paramMap.put("tradeAmt", tradeAmt);

        when(myMbspService.findDetailByBarCdAndFrchId(anyMap()))
                .thenReturn(null);

        // #2. When
        BizException exception =
                assertThrows(BizException.class, () -> myMbspAccumService.accumToBarCd(paramMap));

        // #3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.NOT_FOUND_MBSP_TO_BARCODE);
    }

    @Test
    @DisplayName("멤버십바코드적립_실패_멤버십상태")
    @Transactional
    public void test_accumToBarCd_fail_active_mbsp() throws Exception {
        // #1. Given
        int tradeAmt = 10000;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("barCd", BAR_CD);
        paramMap.put("frchId", FRCH_ID);
        paramMap.put("tradeAmt", tradeAmt);

        when(myMbspService.findDetailByBarCdAndFrchId(anyMap()))
                .thenReturn(MyMbspDetailInfoVo.builder()
                        .mbspInfo(MbspVO.builder().status(Status.STOP.getCode()).build())
                        .build());

        // #2. When
        BizException exception =
                assertThrows(BizException.class, () -> myMbspAccumService.accumToBarCd(paramMap));

        // #3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.NOT_ACTIVE_MBSP);
    }

    @Test
    @DisplayName("멤버십바코드적립_실패_가맹점상태")
    @Transactional
    public void test_accumToBarCd_fail_active_frch() throws Exception {
        // #1. Given
        int tradeAmt = 10000;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("barCd", BAR_CD);
        paramMap.put("frchId", FRCH_ID);
        paramMap.put("tradeAmt", tradeAmt);

        when(myMbspService.findDetailByBarCdAndFrchId(anyMap()))
                .thenReturn(MyMbspDetailInfoVo.builder()
                        .mbspInfo(MbspVO.builder().status(Status.USE.getCode()).build())
                        .frchInfo(MbspFrchVO.builder().status(Status.STOP.getCode()).build())
                        .build());

        // #2. When
        BizException exception =
                assertThrows(BizException.class, () -> myMbspAccumService.accumToBarCd(paramMap));

        // #3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.NOT_ACTIVE_FRANCHISEE);
    }


    @Test
    @DisplayName("멤버십바코드적립_성공")
    @Transactional
    public void test_accumToBarCd_success() throws Exception {
        // #1. Given
        int tradeAmt = 10000;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("barCd", BAR_CD);
        paramMap.put("frchId", FRCH_ID);
        paramMap.put("tradeAmt", tradeAmt);

        when(myMbspService.findDetailByBarCdAndFrchId(anyMap()))
                .thenReturn(MyMbspDetailInfoVo.builder()
                        .acntId(ACNT_ID)
                        .mbspId(MBSP_ID)
                        .mbspInfo(MbspVO.builder().status(Status.USE.getCode()).activeMonths(2).build())
                        .frchInfo(MbspFrchVO.builder().status(Status.USE.getCode()).build())
                        .mbspGrdInfo(MbspGrdVO.builder().accumRat(3).build())
                        .build());

        when(acntService.findByIdAndCheckActive(anyLong()))
                .thenReturn(AcntVO.builder().build());

        doNothing().when(myMbspAccumMapper).save(any(MyMbspAccumVO.class));

        when(myMbspService.createBarCd())
                .thenReturn("123456777");


        // #2. When
        String cancelBarCd = myMbspAccumService.accumToBarCd(paramMap);

        // #3. Then
        assertThat(cancelBarCd).isNotNull();

        verify(myMbspService, times(1)).findDetailByBarCdAndFrchId(anyMap());
        verify(acntService, times(1)).findByIdAndCheckActive(anyLong());
        verify(myMbspService, times(1)).createBarCd();
        verify(myMbspAccumMapper, times(1)).save(any(MyMbspAccumVO.class));
    }


}
