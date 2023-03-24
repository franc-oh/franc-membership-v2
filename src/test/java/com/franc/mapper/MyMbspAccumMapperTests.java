package com.franc.mapper;

import com.franc.code.MbspGrd;
import com.franc.code.Status;
import com.franc.util.DateUtil;
import com.franc.util.NumberUtil;
import com.franc.vo.MyMbspAccumVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {"spring.profiles.active=test", "jasypt.encryptor.password=test"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MyMbspAccumMapperTests {

    @Autowired
    private MyMbspAccumMapper myMbspAccumMapper;

    @Autowired
    private MyMbspMapper myMbspMapper;

    private static final Long ACNT_ID = 5L;
    private static final String MBSP_ID = "M230227000001";
    private static final String FRCH_ID = "F230228000003";
    private static final String BAR_CD = "1234567";



    @Test
    @DisplayName("멤버십_바코드_적립 + 적립 키 조회")
    @Transactional
    public void test_save_and_findById() throws Exception {
        // #1. Given
        String cancelBarCd = DateUtil.getNow() + getBarCdSeq();
        int tradeAmt = 10000;

        MyMbspAccumVO accumVo = buildVO(cancelBarCd, tradeAmt);


        // #2. When
        myMbspAccumMapper.save(accumVo);
        MyMbspAccumVO resultVo = myMbspAccumMapper.findById(cancelBarCd);

        // #3. Then
        assertThat(resultVo).isNotNull();
        assertThat(resultVo.getCancelBarCd()).isEqualTo(cancelBarCd);
        assertThat(resultVo.getTradeAmt()).isEqualTo(tradeAmt);
        assertThat(resultVo.getStatus()).isEqualTo(Status.USE.getCode());
        assertThat(resultVo.getStatusNm()).isEqualTo(Status.USE.getName());

    }

    @Test
    @DisplayName("멤버십_바코드_적립취소 + 적립 키 조회")
    @Transactional
    public void test_update_and_findById() throws Exception {
        // #1. Given
        String cancelBarCd = DateUtil.getNow() + getBarCdSeq();
        int tradeAmt = 10000;

        MyMbspAccumVO accumVo = buildVO(cancelBarCd, tradeAmt);

        myMbspAccumMapper.save(accumVo);
        MyMbspAccumVO saveVo = myMbspAccumMapper.findById(cancelBarCd);

        // #2. When
        saveVo.cancelAccum();
        myMbspAccumMapper.modify(saveVo);
        MyMbspAccumVO modifyVo = myMbspAccumMapper.findById(cancelBarCd);

        // #3. Then
        assertThat(modifyVo).isNotNull();
        assertThat(modifyVo.getCancelBarCd()).isEqualTo(cancelBarCd);
        assertThat(modifyVo.getTradeAmt()).isEqualTo(tradeAmt);
        assertThat(modifyVo.getStatus()).isEqualTo(Status.STOP.getCode());
        assertThat(modifyVo.getStatusNm()).isEqualTo(Status.STOP.getName());
        assertThat(modifyVo.getAccumCancelDate()).isNotNull();

    }




    public Integer getBarCdSeq() throws Exception {
        return myMbspMapper.getBarCdSeq();
    }

    public MyMbspAccumVO buildVO(String cancelBarCd, int tradeAmt) throws Exception {
        int accumRat = 3;
        return MyMbspAccumVO.builder()
                .cancelBarCd(cancelBarCd)
                .acntId(ACNT_ID)
                .mbspId(MBSP_ID)
                .frchId(FRCH_ID)
                .tradeAmt(tradeAmt)
                .accumRat(accumRat)
                .accumPoint(NumberUtil.getCalcPerAmt(tradeAmt, accumRat))
                .expireYmd(DateUtil.getAddMonth(2))
                .mbspGrdCd(MbspGrd.COMMON.getCode())
                .build();
    }

}
