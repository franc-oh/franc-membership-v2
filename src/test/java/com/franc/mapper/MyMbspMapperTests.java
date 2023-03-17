package com.franc.mapper;

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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=franc_msp"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MyMbspMapperTests {

    @Autowired
    private MyMbspMapper myMbspMapper;

    private static final Long ACNT_ID = 5L;
    private static final String MBSP_ID = "M230227000001";
    private static final String BAR_CD = "1234567";

    /*
        1.사용자 체크*
        2.가입
            - 나의 멤버십 가져오기*
                - 신규 건이 있는 경우 => 중복
                - 탈퇴 건이 있는 경우 => 재가입*
                - 없는 경우 => 바코드 생성*, 등록*
     */

    @Test
    @DisplayName("나의멤버십등록 + 나의멤버십조회")
    @Transactional
    public void save_and_findById() throws Exception {
        // #1. Given
        MyMbspVO saveVO = MyMbspVO.builder()
                .acntId(ACNT_ID)
                .mbspId(MBSP_ID)
                .barCd(BAR_CD)
                .build();

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

}
