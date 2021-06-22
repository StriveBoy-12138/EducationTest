package com.njbandou.web.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Dscription: 分布式锁（redis）解决重复提交
 * @Modified By:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {

    // redis 锁 key 前缀
    String prefix() default "";

    // 过期时间，默认5秒
    int expire() default 5;

    // 时间单位，默认“秒”
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    // key 的分隔符 默认“:”
    String delimiter() default ":";
}
