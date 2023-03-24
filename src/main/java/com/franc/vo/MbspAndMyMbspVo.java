package com.franc.vo;

import com.franc.code.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MbspAndMyMbspVo extends MbspVO {

    private String myMbspYn;
    private MyMbspInfo myMbspInfo;


    @Getter
    @Setter
    @ToString(callSuper = true)
    @NoArgsConstructor
    @SuperBuilder
    public static class MyMbspInfo extends MyMbspVO {

    }
}
