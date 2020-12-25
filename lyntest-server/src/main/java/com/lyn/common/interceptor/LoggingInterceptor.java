package com.lyn.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author 简单随风
 * @date 2020/9/11
 * okhttp请求拦截器
 */
@Slf4j
public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long startTime = System.nanoTime();
        log.info(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));

        Response response =  chain.proceed(request);

        long endTime = System.nanoTime();
        log.info(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (endTime - startTime) / 1e6d, response.headers()));

        return response;
    }
}
