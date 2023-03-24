package com.franc.vo;

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


}
