package com.flab.delivery.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheConstants {

    public static final String CATEGORY = "CATEGORY";

    public static final Duration DEFAULT_EXPIRE_TIME = Duration.ofMinutes(10L);
    public static final Duration CATEGORY_EXPIRE_TIME = Duration.ofDays(1L);


}
