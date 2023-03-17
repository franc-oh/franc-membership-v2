package com.franc.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=franc_msp"})
@ActiveProfiles("test")
public class H2FunctionConfigTests {

    @Test
    @DisplayName("STR_TO_DATE")
    public void str_to_date() throws Exception {
        // #1. Given
        String ymd = "20230317132030";
        String format = "%Y%m%d%H%i%s";

        // #2. When
        Date date = H2FunctionConfig.strToDate(ymd, format);

        // #3. Then
        assertThat(date).isNotNull();
        System.out.println(date);
    }

}
