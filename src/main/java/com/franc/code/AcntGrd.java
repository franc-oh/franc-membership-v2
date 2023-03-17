package com.franc.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcntGrd {

    ADMIN("ADMIN", "관리자"),
    SELLER("SELLER", "판매자"),
    USER("USER", "사용자");

    private final String code;
    private final String name;

    public static AcntGrd of(final String code) {
        return AcntGrd.of(code);
    }


}
