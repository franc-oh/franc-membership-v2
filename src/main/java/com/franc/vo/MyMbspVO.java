package com.franc.vo;

import com.franc.code.MbspGrd;
import com.franc.code.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(of = {"acntId", "mbspId"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MyMbspVO {

    private Long acntId;
    private String mbspId;

    @Builder.Default
    private Character status = Status.USE.getCode();

    @Builder.Default
    private Integer totalAccumPoint = 0;

    @Builder.Default
    private String mbspGrdCd = MbspGrd.COMMON.getCode();
    private LocalDateTime insertDate;

    @Builder.Default
    private LocalDateTime withdrawalDate = null;
    private String barCd;


    // 조회용
    private String statusNm;
    private String mbspGrdNm;




    public void setStatus(Character status) {
        this.status = status;
        this.statusNm = Status.of(status).getName();
    }

    public void setMbspGrdCd(String mbspGrdCd) {
        this.mbspGrdCd = mbspGrdCd;
        this.mbspGrdNm = MbspGrd.of(mbspGrdCd).getName();
    }


    /** 바코드 셋팅 */
    public void setBarCd(String barCd) {
        this.barCd = barCd;
    }

    /** 재가입 */
    public boolean rejoin() {
        boolean result = false;
        if(this.acntId != null && StringUtils.hasText(this.mbspId)) {
            this.withdrawalDate = null;
            this.setStatus(Status.USE.getCode());
            result = true;
        }

        return result;
    }

    /** 탈퇴 */
    public boolean withdrawal() {
        boolean result = false;
        if(this.acntId != null && StringUtils.hasText(this.mbspId)) {
            this.withdrawalDate = LocalDateTime.now();
            this.setStatus(Status.USE.getCode());
            result = true;
        }

        return result;
    }

}
