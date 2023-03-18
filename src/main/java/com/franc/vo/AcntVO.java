package com.franc.vo;

import com.franc.code.AcntGrd;
import com.franc.code.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(of = "acntId")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AcntVO {

    private Long acntId;
    private String acntNm;

    @Builder.Default
    private Character status = Status.USE.getCode();
    private String birth;
    private String hphone;
    private String email;

    @Builder.Default
    private String acntGrd = AcntGrd.USER.getCode();
    private LocalDateTime insertDate;
    private String insertUser;
    private LocalDateTime updateDate;
    private String updateUser;
}
