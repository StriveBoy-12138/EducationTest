package com.njbandou.web.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @Dscription: key 生成器
 */
public interface CacheKeyGenerator {
    // 获取AOP参数，生成指定缓存key
    String getLockKey(ProceedingJoinPoint pjp);
}
