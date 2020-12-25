package com.lyn.common.mapping;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @author 简单随风
 * @date 2020/7/20
 */
public class AutoPrefixUrlMapping extends RequestMappingHandlerMapping {

    private static final String API_PACKAGE_PATH = "com.lyn.controller";

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {

        RequestMappingInfo mappingInfo =  super.getMappingForMethod(method, handlerType);
        if (mappingInfo != null){
            String prefix = this.getPrefix(handlerType);
            return RequestMappingInfo.paths(prefix).build().combine(mappingInfo);
        }
        return mappingInfo;
    }

    // 获取前缀
    private String getPrefix(Class<?> handlerType){

        String packageName = handlerType.getPackage().getName();
        String newPath = packageName.replaceAll(API_PACKAGE_PATH, "");

        return newPath.replace(".","/");
    }
}
