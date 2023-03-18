package com.franc.mapper;

import com.franc.vo.MbspVO;
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
public class MbspMapperTests {

    @Autowired
    private MbspMapper mbspMapper;

    private static final String MBSP_ID = "M230227000001";

    @Test
    @DisplayName("멤버십_가져오기")
    public void findById() throws Exception {
        // #1. Given

        // #2. When
        MbspVO mbspVO = mbspMapper.findById(MBSP_ID);

        // #3. Then
        assertThat(mbspVO).isNotNull();
        assertThat(mbspVO.getMbspId()).isEqualTo(MBSP_ID);

    }
}
