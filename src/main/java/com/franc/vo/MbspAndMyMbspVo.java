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
    @AllArgsConstructor
    @SuperBuilder
    public static class MyMbspInfo extends MyMbspVO {


        private String statusNm;

        public void setStatusNm() {
            Character status = this.getStatus();
            if(status != null) {
                this.statusNm = Status.of(status).getName();
            }
        }
    }
}
