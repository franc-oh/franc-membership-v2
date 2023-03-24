package com.franc.mapper;

import com.franc.code.Status;
import com.franc.vo.MyMbspVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=test"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MyMbspMapperTests {

    @Autowired
    private MyMbspMapper myMbspMapper;

    private static final Long ACNT_ID = 5L;
    private static final String MBSP_ID = "M230227000001";
    private static final String BAR_CD = "1234567";



    @Test
    @DisplayName("나의멤버십등록 + 나의멤버십조회")
    @Transactional
    public void test_save_and_findById() throws Exception {
        // #1. Given
        MyMbspVO saveVO = buildVo(new HashMap<>());

        // #2. When
        myMbspMapper.save(saveVO);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("acntId", ACNT_ID);
        paramMap.put("mbspId", MBSP_ID);
        MyMbspVO resultVO = myMbspMapper.findById(paramMap);

        // #3. Then
        assertThat(resultVO).isNotNull();
        assertThat(resultVO.getAcntId()).isEqualTo(ACNT_ID);
        assertThat(resultVO.getMbspId()).isEqualTo(MBSP_ID);
        assertThat(resultVO.getInsertDate()).isNotNull();
    }

    @Test
    @DisplayName("멤버십_재가입")
    @Transactional
    public void test_rejoin() throws Exception {
        // #1. Given
        Long acntId = ACNT_ID;
        String mbspId = MBSP_ID;
        String barCd = BAR_CD;
        LocalDateTime withdrawalDate = LocalDateTime.now();
        Character status = Status.WITHDRAWAL.getCode();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("acntId", acntId);
        paramMap.put("mbspId", mbspId);
        paramMap.put("barCd", barCd);
        paramMap.put("withdrawalDate", withdrawalDate);
        paramMap.put("status", status);

        MyMbspVO myMbspVO = buildVo(paramMap);

        myMbspMapper.save(myMbspVO);

        // #2. When
        if(myMbspVO.rejoin())
            myMbspMapper.modify(myMbspVO);

        Map<String, Object> resultParamMap = new HashMap<>();
        resultParamMap.put("acntId", acntId);
        resultParamMap.put("mbspId", mbspId);
        MyMbspVO resultVO = myMbspMapper.findById(resultParamMap);

        // #3. Then
        assertThat(resultVO.getAcntId()).isEqualTo(acntId);
        assertThat(resultVO.getMbspId()).isEqualTo(mbspId);
        assertThat(resultVO.getStatus()).isEqualTo(Status.USE.getCode());
        assertThat(resultVO.getWithdrawalDate()).isNull();
    }

    @Test
    @DisplayName("바코드시퀀스생성")
    public void test_getBarCdSeq() throws Exception {
        // #1. Given

        // #2. When
        int seq1 = getBarCdSeq();
        int seq2 = getBarCdSeq();
        int seq3 = getBarCdSeq();

        // #3. Then
        assertThat(seq1).isEqualTo(1);
        assertThat(seq2).isEqualTo(2);
        assertThat(seq3).isEqualTo(3);
    }

    @Test
    @DisplayName("멤버십_탈퇴")
    @Transactional
    public void test_withdrawal() throws Exception {
        // #1. Given
        MyMbspVO vo = buildVo(new HashMap<>());
        myMbspMapper.save(vo);

        // #2. When
        if(vo.withdrawal())
            myMbspMapper.modify(vo);

        Map<String, Object> resultParamMap = new HashMap<>();
        resultParamMap.put("acntId", ACNT_ID);
        resultParamMap.put("mbspId", MBSP_ID);
        MyMbspVO resultVO = myMbspMapper.findById(resultParamMap);

        // #3. Then
        assertThat(resultVO).isNotNull();
        assertThat(resultVO.getAcntId()).isEqualTo(ACNT_ID);
        assertThat(resultVO.getMbspId()).isEqualTo(MBSP_ID);
        assertThat(resultVO.getStatus()).isEqualTo(Status.WITHDRAWAL.getCode());
        assertThat(resultVO.getWithdrawalDate()).isNotNull();

    }

    @Test
    @DisplayName("바코드로멤버십상세정보찾기")
    @Transactional
    public void findDetailByBarCdAndFrchId() throws Exception {
        // #1. Given
        MyMbspVO vo = buildVo(new HashMap<>());
        myMbspMapper.save(vo);

        // #2. When
        MyMbspVO resultVO = myMbspMapper.findByBarCd(BAR_CD);

        // #.3 Then
        assertThat(resultVO).isNotNull();
        assertThat(resultVO.getAcntId()).isEqualTo(ACNT_ID);
        assertThat(resultVO.getMbspId()).isEqualTo(MBSP_ID);
    }


    public MyMbspVO buildVo(Map<String, Object> paramMap) throws Exception {
        Long acntId = ACNT_ID;
        String mbspId = MBSP_ID;
        String barCd = BAR_CD;
        LocalDateTime withdrawalDate = null;
        Character status = null;

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

    public Integer getBarCdSeq() throws Exception {
        return myMbspMapper.getBarCdSeq();
    }

}
