package com.franc.vo;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MyMbspDetailInfoVo extends MyMbspVO {
    private MbspFrchVO frchInfo;

    private MbspVO mbspInfo;

    private MbspGrdVO mbspGrdInfo;

}
