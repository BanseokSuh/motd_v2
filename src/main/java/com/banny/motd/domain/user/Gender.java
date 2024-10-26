package com.banny.motd.domain.user;

import com.banny.motd.global.dto.response.ApiResponseStatusType;
import com.banny.motd.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {

    MALE("남자"),
    FEMALE("여자"),
    ETC("기타");

    private final String text;

    public static Gender from(String gender) {
        for (Gender g : Gender.values()) {
            if (g.name().equalsIgnoreCase(gender)) {
                return g;
            }
        }

        throw new ApplicationException(ApiResponseStatusType.FAIL_INVALID_PARAMETER, "Invalid gender");
    }
}