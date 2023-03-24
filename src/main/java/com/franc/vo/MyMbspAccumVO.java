package com.franc.vo;


import com.franc.code.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(of = "cancelBarCd")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MyMbspAccumVO {

    private String cancelBarCd;

    @Builder.Default
    private Character status = Status.USE.getCode();

    private Long acntId;
    private String mbspId;
    private String frchId;
    private Integer tradeAmt;
    private String mbspGrdCd;
    private Integer accumRat;
    private Integer accumPoint;
    private String expireYmd;
    private LocalDateTime accumDate;
    private LocalDateTime accumCancelDate;


    // 조회용
    private String statusNm;



    public void setStatus(Character status) {
        this.status = status;
        this.statusNm = Status.of(status).getName();
    }


    /** 적립 취소 */
    public boolean cancelAccum() throws Exception {
        boolean result = false;

        if(StringUtils.hasText(this.cancelBarCd)) {
            this.setStatus(Status.STOP.getCode());
            this.accumCancelDate = LocalDateTime.now();
            result = true;
        }

        return result;
    }
}
