package com.franc.vo;

import com.franc.code.MbspGrd;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(of = {"mbspId", "mbspGrdCd"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MbspGrdVO {

    private String mbspId;
    private String mbspGrdCd;
    private Integer grdUpPointFr;
    private Integer grdUpPointTo;
    private Integer accumRat;
    private Integer discRat;
    private LocalDateTime insertDate;
    private String insertUser;
    private LocalDateTime updateDate;
    private String updateUser;


    // 조회용
    private String mbspGrdNm;





    public void setMbspGrdCd(String mbspGrdCd) {
        this.mbspGrdCd = mbspGrdCd;
        this.mbspGrdNm = MbspGrd.of(mbspGrdCd).getName();
    }

}
