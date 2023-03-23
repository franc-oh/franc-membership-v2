package com.franc.util;

import com.franc.code.Code;

import java.util.HashMap;
import java.util.Map;

public class PageUtil {



    /**
     * 페이지 관련 계산하여 리턴
     * @param pageNo
     * @param pageLimit
     * @return resultMap
     * @throws Exception
     */
    public static Map<String, Object> getPageMap(Integer pageNo, Integer pageLimit) throws Exception {
        int offset = 0;
        int limit = 0;

        if(pageNo == null || pageNo <= 0) pageNo = Code.DEFAULT_PAGE_NO;
        if(pageLimit == null || pageLimit <= 0) pageLimit = Code.DEFAULT_PAGE_LIMIT;

        offset = Math.abs(pageNo -1) * pageLimit;
        limit = pageLimit;

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("offset", offset);
        resultMap.put("limit", limit);

        return resultMap;
    }

}
