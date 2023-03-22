package com.franc.service;

import com.franc.code.Status;
import com.franc.exception.BizException;
import com.franc.exception.ExceptionResult;
import com.franc.mapper.MbspMapper;
import com.franc.vo.MbspVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MbspServiceTests {

    @InjectMocks
    private MbspService mbspService;

    @Mock
    private MbspMapper mbspMapper;

    private static final String MBSP_ID = "M230227000001";

    @Test
    @DisplayName("멤버십정보조회_및_체크_실패_없음")
    public void test_findByIdAndCheckActive_fail_not_found() throws Exception {
        // #1. Given
        String mbspId = "1111";
        when(mbspMapper.findById(anyString()))
                .thenReturn(null);

        // #2. When
        BizException exception =
                assertThrows(BizException.class, () -> mbspService.findByIdAndCheckActive(mbspId));

        // # 3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.NOT_FOUND_MBSP);

    }


    @Test
    @DisplayName("멤버십정보조회_및_체크_실패_정지")
    public void test_findByIdAndCheckActive_fail_not_active() throws Exception {
        // #1. Given
        when(mbspMapper.findById(anyString()))
                .thenReturn(MbspVO.builder()
                        .mbspId(MBSP_ID)
                        .status(Status.STOP.getCode())
                        .build());

        // #2. When
        BizException exception =
                assertThrows(BizException.class, () -> mbspService.findByIdAndCheckActive(MBSP_ID));

        // # 3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.NOT_ACTIVE_MBSP);
    }

    @Test
    @DisplayName("멤버십정보조회_및_체크_성공")
    public void test_findByIdAndCheckActive_success() throws Exception {
        // #1. Given
        when(mbspMapper.findById(anyString()))
                .thenReturn(MbspVO.builder()
                        .mbspId(MBSP_ID)
                        .status(Status.USE.getCode())
                        .build());

        // #2. When
        MbspVO vo = mbspService.findByIdAndCheckActive(MBSP_ID);

        // # 3. Then
        assertThat(vo).isNotNull();
        assertThat(vo.getMbspId()).isEqualTo(MBSP_ID);
        assertThat(vo.getStatus()).isEqualTo(Status.USE.getCode());

    }

}
