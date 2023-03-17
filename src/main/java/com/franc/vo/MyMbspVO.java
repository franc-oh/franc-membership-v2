package com.franc.vo;

import com.franc.code.MbspGrd;
import com.franc.code.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter @Setter
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
    private LocalDateTime withdrawalDate;
    private String barCd;


    public void setBarCd(String barCd) {
        this.barCd = barCd;
    }

}
