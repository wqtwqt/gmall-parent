package com.atguigu.gmall.common.cache;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.constant.RedisConst;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class GmallCacheAspect {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @SneakyThrows
    @Around("@annotation(com.atguigu.gmall.common.cache.GmallCache)")
    public Object cacheAroundAdvice(ProceedingJoinPoint joinPoint){
        Object object = new Object();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        GmallCache gmallCache = methodSignature.getMethod().getAnnotation(GmallCache.class);
        String prefix = gmallCache.prefix();
        Object[] args = joinPoint.getArgs();
        String key = prefix + Arrays.asList(args).toString();
        try {
            object = cacheHit(key,methodSignature);
            if (object==null){
                String keyLock = prefix + ":lock";
                RLock lock = redissonClient.getLock(keyLock);
                boolean b = lock.tryLock(RedisConst.SKULOCK_EXPIRE_PX1, RedisConst.SKULOCK_EXPIRE_PX2, TimeUnit.SECONDS);
                if (b){
                    try {
                        object = joinPoint.proceed(joinPoint.getArgs());

                        if (object == null){
                            Object object1 = new Object();
                            redisTemplate.opsForValue().set(key,JSON.toJSONString(object1),RedisConst.SKUKEY_TEMPORARY_TIMEOUT,TimeUnit.SECONDS);
                            return object1;
                        }else {
                            redisTemplate.opsForValue().set(key,JSON.toJSONString(object),RedisConst.SKUKEY_TIMEOUT,TimeUnit.SECONDS);
                            return object;
                        }
                    } finally {
                        lock.unlock();
                    }
                }else {
                    Thread.sleep(1000);
                    return cacheAroundAdvice(joinPoint);
                }
            }else {
                return object;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

    private Object cacheHit(String key, MethodSignature methodSignature) {
        String strJson = (String) redisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(strJson)){
            Class returnType = methodSignature.getReturnType();
            return JSON.parseObject(strJson,returnType);
        }
        return null;
    }

}
