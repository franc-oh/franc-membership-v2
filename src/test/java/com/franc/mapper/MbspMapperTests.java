package com.franc.mapper;

import com.franc.code.Status;
import com.franc.util.PageUtil;
import com.franc.vo.MbspAndMyMbspVo;
import com.franc.vo.MbspVO;
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
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=test"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MbspMapperTests {

    @Autowired
    private MbspMapper mbspMapper;

    @Autowired
    private MyMbspMapper myMbspMapper;


    private static final String MBSP_ID = "M230227000002";

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

    @Test
    @DisplayName("멤버십리스트_및_가입여부_가져오기")
    @Transactional
    public void findAllAndMyMbspYn() throws Exception {
        // #1. Given
        Long acntId = 5L;
        Integer pageNo = 1;
        Integer pageLimit = 10;

        Map<String, Object> paramMap = PageUtil.getPageMap(pageNo, pageLimit);
        paramMap.put("acntId", acntId);


        myMbspMapper.save(MyMbspVO.builder()
                .acntId(acntId)
                .mbspId(MBSP_ID)
                .barCd("11111")
                .build());


        // #2. When
        List<MbspAndMyMbspVo> mbspVOList = mbspMapper.findAllAndMyMbspYn(paramMap);


        // #3. Then
        assertThat(mbspVOList.size()).isEqualTo(2);
        assertThat(mbspVOList.get(0).getMyMbspYn()).isEqualTo("N");
        assertThat(mbspVOList.get(1).getMyMbspYn()).isEqualTo("Y");
    }


    @Test
    @DisplayName("멤버십정보_및_가입정보_가져오기")
    @Transactional
    public void findByIdAndMyMbspInfo() throws Exception {
        // #1. Given
        Long acntId = 5L;
        String barCd = "123456";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("acntId", acntId);
        paramMap.put("mbspId", MBSP_ID);

        myMbspMapper.save(MyMbspVO.builder()
                .acntId(acntId)
                .mbspId(MBSP_ID)
                .barCd(barCd)
                .build());

        // #2. When
        MbspAndMyMbspVo mbspAndMyMbspVo = mbspMapper.findByIdAndMyMbspInfo(paramMap);

        // #3. Then
        assertThat(mbspAndMyMbspVo).isNotNull();
        assertThat(mbspAndMyMbspVo.getMbspId()).isEqualTo(MBSP_ID);
        assertThat(mbspAndMyMbspVo.getMyMbspInfo()).isNotNull();
        assertThat(mbspAndMyMbspVo.getMyMbspInfo().getAcntId()).isEqualTo(acntId);
        assertThat(mbspAndMyMbspVo.getMyMbspInfo().getStatus()).isEqualTo(Status.USE.getCode());
        assertThat(mbspAndMyMbspVo.getMyMbspInfo().getBarCd()).isEqualTo(barCd);
    }

}
