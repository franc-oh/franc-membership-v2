package com.franc.vo;

import com.franc.code.Code;
import com.franc.code.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
@EqualsAndHashCode(of = {"mbspId"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MbspVO {

    private String mbspId;

    private String mbspNm;

    @Builder.Default
    private Character status = Status.USE.getCode();
    private String mbspInfo;
    private String mbspImgUrl;
    private String homepageUrl;

    @Builder.Default
    private Integer activeMonths = Code.MEMBERSHIP_ACTIVE_MONTHS;
    private String bigo;
    private LocalDateTime insertDate;
    private String insertUser;
    private LocalDateTime updateDate;
    private String updateUser;

}
