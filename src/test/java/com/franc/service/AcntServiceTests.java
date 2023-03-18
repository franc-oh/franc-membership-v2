package com.franc.service;

import com.franc.code.Status;
import com.franc.exception.BizException;
import com.franc.exception.ExceptionResult;
import com.franc.mapper.AcntMapper;
import com.franc.vo.AcntVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AcntServiceTests {

    @InjectMocks
    private AcntService acntService;

    @Mock
    private AcntMapper acntMapper;



    @Test
    @DisplayName("회원정보조회_및_체크_실패_없음")
    public void test_findByIdAndCheckActive_fail_not_found() throws Exception {
        // #1. Given
        Long acntId = 7L;
        when(acntMapper.findById(anyLong()))
                .thenReturn(null);

        // #2. When
        BizException exception =
                assertThrows(BizException.class, () -> acntService.findByIdAndCheckActive(acntId));

        // # 3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.NOT_FOUND_ACNT);

    }

    @Test
    @DisplayName("회원정보조회_및_체크_실패_비활성계정")
    public void test_findByIdAndCheckActive_fail_not_active() throws Exception {
        // #1. Given
        Long acntId = 5L;
        when(acntMapper.findById(anyLong()))
                .thenReturn(AcntVO.builder()
                        .acntId(acntId)
                        .status(Status.STOP.getCode())
                        .build());

        // #2. When
        BizException exception =
                assertThrows(BizException.class, () -> acntService.findByIdAndCheckActive(acntId));

        // # 3. Then
        assertThat(exception.getClass()).isEqualTo(BizException.class);
        assertThat(exception.getResult()).isEqualTo(ExceptionResult.NOT_ACTIVE_ACNT);

    }

    @Test
    @DisplayName("회원정보조회_및_체크_성공")
    public void test_findByIdAndCheckActive_success() throws Exception {
        // #1. Given
        Long acntId = 5L;
        when(acntMapper.findById(anyLong()))
                .thenReturn(AcntVO.builder()
                        .acntId(acntId)
                        .status(Status.USE.getCode())
                        .build());

        // #2. When
        AcntVO vo = acntService.findByIdAndCheckActive(acntId);

        // # 3. Then
        assertThat(vo).isNotNull();
        assertThat(vo.getAcntId()).isEqualTo(acntId);
        assertThat(vo.getStatus()).isEqualTo(Status.USE.getCode());

    }

}
