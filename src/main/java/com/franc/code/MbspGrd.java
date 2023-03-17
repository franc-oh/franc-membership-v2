package com.franc.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MbspGrd {

    COMMON("COMMON", "일반"),
    BRONZE("BRONZE", "브론즈"),
    SILVER("SILVER", "실버"),
    GOLD("GOLD", "골드"),
    VIP("VIP", "VIP");

    private final String code;
    private final String name;

    public static MbspGrd of(final String code) {
        return MbspGrd.of(code);
    }


}
