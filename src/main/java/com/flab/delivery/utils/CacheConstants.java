package com.flab.delivery.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheConstants {

    public static final String CATEGORY = "CATEGORY";
    public static final String STORE_LIST = "STORE_LIST";

    public static final Duration DEFAULT_EXPIRE_TIME_MIN = Duration.ofMinutes(10L);
    public static final Duration CATEGORY_EXPIRE_TIME_MIN = Duration.ofMinutes(20L);


}
