package com.franc.dto;

import com.franc.code.Code;
import lombok.*;

import javax.validation.constraints.Min;
import java.util.List;

public class MbspFindAllDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Request {
        @Min(1)
        private Long acntId;
        @Min(1)
        private Integer pageNo = Code.DEFAULT_PAGE_NO;
        @Min(1)
        private Integer pageLimit = Code.DEFAULT_PAGE_LIMIT;


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

        private Integer mbspCnt;

        private List<MbspInfo> mbspList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class MbspInfo {

        private String mbspId;
        private String mbspNm;
        private String mbspInfo;
        private String mbspImgUrl;
        private String myMbspYn;

    }
}
