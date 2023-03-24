package com.franc.vo;

import com.franc.code.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(of = {"mbspId", "frchId"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MbspFrchVO {

    private String mbspId;
    private String frchId;
    private String frchNm;
    @Builder.Default
    private Character status = Status.USE.getCode();

    private String zipCd;
    private String addr1;
    private String addr2;
    private String telNo;
    private String bigo;

    private LocalDateTime insertDate;
    private String insertUser;
    private LocalDateTime updateDate;
    private String updateUser;



    // 조회용
    private String statusNm;




    public void setStatus(Character status) {
        this.status = status;
        this.statusNm = Status.of(status).getName();
    }


}
