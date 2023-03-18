package com.franc.mapper;

import com.franc.vo.AcntVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=test"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class AcntMapperTests {

    @Autowired
    private AcntMapper acntMapper;

    private static final Long ACNT_ID = 5L;

    @Test
    @DisplayName("회원정보_가져오기")
    public void findById() throws Exception {
        // #1. Given

        // #2. When
        AcntVO acntVO = acntMapper.findById(ACNT_ID);

        // #3. Then
        assertThat(acntVO).isNotNull();
        assertThat(acntVO.getAcntId()).isEqualTo(ACNT_ID);

    }
}
