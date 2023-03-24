package com.franc.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Status {

    USE('1', "사용"),
    STOP('9', "정지"),
    WITHDRAWAL('0', "탈퇴");

    private final Character code;
    private final String name;


    public Character code() {
        return code;
    }

    public static final Map<Character, Status> CACHED_STATUS =
            Stream.of(values()).collect(Collectors.toMap(Status::code, e -> e));

    public static Status of(final Character code) {
        return CACHED_STATUS.get(code);
    }

}
