package com.lyntest.utils;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author 简单随风
 * @date 2020/1/3
 */
public class MultiThreadingListener implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {

        // 设置线程执行次数(重复次数 * 线程池内线程数)
        annotation.setInvocationCount(ApiTestConfig.repeatTimes * ApiTestConfig.threadPoolSize);
        // 线程池内线程数
        annotation.setThreadPoolSize(ApiTestConfig.threadPoolSize);
    }
}
