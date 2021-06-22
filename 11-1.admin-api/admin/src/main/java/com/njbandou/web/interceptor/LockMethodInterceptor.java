package com.njbandou.web.interceptor;

import com.njbandou.web.annotation.CacheLock;
import com.njbandou.web.exception.CacheLockException;
import com.njbandou.web.exception.ServerException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

@Aspect
@Configuration
public class LockMethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LockMethodInterceptor.class);

    @Autowired
    public LockMethodInterceptor(StringRedisTemplate lockRedisTemplate, CacheKeyGenerator cacheKeyGenerator) {
        this.lockRedisTemplate = lockRedisTemplate;
        this.cacheKeyGenerator = cacheKeyGenerator;
    }

    private final StringRedisTemplate lockRedisTemplate;
    private final CacheKeyGenerator cacheKeyGenerator;


    @Around("execution(public * *(..)) && @annotation(com.njbandou.web.annotation.CacheLock)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lock = method.getAnnotation(CacheLock.class);

        if (StringUtils.isEmpty(lock.prefix())) {
            throw new ServerException("锁 key 不能为空");
        }

        final String lockKey = cacheKeyGenerator.getLockKey(pjp);

        try {
            // 采用原生 API 来实现分布式锁
            final Boolean success = lockRedisTemplate.execute((RedisCallback<Boolean>) connection ->
                    connection.set(lockKey.getBytes(), new byte[0],
                            Expiration.from(lock.expire(), lock.timeUnit()),
                            RedisStringCommands.SetOption.SET_IF_ABSENT));

            if (!success) {
                // 自定义重复提交异常
                throw new CacheLockException();
            }

            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                if (logger.isDebugEnabled()) {
                    throwable.printStackTrace();
                }
                throw new ServerException(throwable.getMessage());
            }
        } finally {
            // 不论成功与否 或是异常，最后都应该释放锁
            lockRedisTemplate.delete(lockKey);
        }

    }
}
