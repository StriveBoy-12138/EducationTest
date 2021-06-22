package com.njbandou.web.interceptor;

import com.njbandou.web.annotation.CacheLock;
import com.njbandou.web.annotation.CacheParam;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class LockKeyGenerator implements CacheKeyGenerator {
    private static final Logger logger = LoggerFactory.getLogger(LockKeyGenerator.class);
    @Override
    public String getLockKey(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lockAnnotation = method.getAnnotation(CacheLock.class);
        final Object[] args = pjp.getArgs();
        final Parameter[] parameters = method.getParameters();
        StringBuilder builder = new StringBuilder();

        // 默认解析方法里面 CacheParam 注解的属性，如果没有尝试解析实体对象中的
        for (int i = 0; i < parameters.length; i++) {
            final CacheParam annotation = parameters[i].getAnnotation(CacheParam.class);
            if (annotation != null) {
                // 如果注解参数不是 String 或者 Integer 类型，则更改参数获取方法
                if (args[i] instanceof Integer || args[i] instanceof String) {
                    builder.append(lockAnnotation.delimiter()).append(args[i]);
                } else {
                    // 利用反射机制，通过属性名称获取属性值
                    final Field field;
                    try {
                        field = args[i].getClass().getDeclaredField(annotation.name());
                        // 私有变量设置为可访问
                        field.setAccessible(true);
                        builder.append(lockAnnotation.delimiter()).append(field.get(args[i]));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        logger.error("目标对象无法获取属性：{}", annotation.name());
                    }
                }
            }
        }

        if (StringUtils.isEmpty(builder.toString())) {
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                final Object object = args[i];
                final Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields) {
                    final CacheParam annotation = field.getAnnotation(CacheParam.class);
                    if (annotation != null) {
                        field.setAccessible(true);
                        builder.append(lockAnnotation.delimiter()).append(ReflectionUtils.getField(field, object));
                    }
                }
            }
        }
        return lockAnnotation.prefix() + builder.toString();
    }
}
