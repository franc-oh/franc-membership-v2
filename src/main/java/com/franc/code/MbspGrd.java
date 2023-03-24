package com.franc.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    public String code() {
        return code;
    }

    public static final Map<String, MbspGrd> CACHED_MBSP_GRD =
            Stream.of(values()).collect(Collectors.toMap(MbspGrd::code, e -> e));

    public static MbspGrd of(final String code) {
        return CACHED_MBSP_GRD.get(code);
    }


}
