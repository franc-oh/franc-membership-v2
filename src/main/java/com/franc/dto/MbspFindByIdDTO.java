package com.franc.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MbspFindByIdDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Request {
        @Min(1)
        private Long acntId;

        @NotNull
        private String mbspId;

    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Response {
        private String resultCode;
        private String resultMessage;

        private String mbspId;
        private String mbspNm;
        private String mbspInfo;
        private String mbspImgUrl;
        private String homepageUrl;

        private MyMbspInfo myMbspInfo;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MyMbspInfo {

        private String statusNm;
        private String mbspGrdCd;
        private Integer totalAccumPoint;
        private String barCd;

    }
}
