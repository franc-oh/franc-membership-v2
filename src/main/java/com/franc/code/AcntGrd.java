package com.franc.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum AcntGrd {

    ADMIN("ADMIN", "관리자"),
    SELLER("SELLER", "판매자"),
    USER("USER", "사용자");

    private final String code;
    private final String name;



    public String code() {
        return code;
    }

    public static final Map<String, AcntGrd> CACHED_ACNT_GRD =
            Stream.of(values()).collect(Collectors.toMap(AcntGrd::code, e -> e));

    public static AcntGrd of(final String code) {
        return CACHED_ACNT_GRD.get(code);
    }


}
