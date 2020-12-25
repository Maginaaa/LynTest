package com.lyn.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 简单随风
 * @date 2020/7/19
 */
public class EntityUtil {


    /**
     * 对象转map
     * @param object 需要转换的对象
     * @param excludeKeys  需要排除的key
     * @return
     */
    public static HashMap<String,Object> wrapperMap(Object object,Set<String> excludeKeys){


        if (object == null) {
            return null;
        }
        HashMap<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = humpToLine(property.getName());
                // 过滤class属性
                if (!"class".equals(key) && !excludeKeys.contains(key)) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(object);
                    if (!StringUtils.isEmpty(value)){
                        map.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

    /**
     * 对象转map 需要转换的对象
     * @param object
     * @return
     */
    public static HashMap<String,Object> entityToMap(Object object){

        if (object == null) {
            return null;
        }
        HashMap<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class") && !key.equals("pageNo") && !key.equals("pageSize")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(object);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

    public static HashMap<String, Object> jsonStrToMap(String jsonStr){
        HashMap<String, Object> map = new HashMap<>();
        JSONObject json = JSONObject.parseObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k);
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<>();
                Iterator it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    Object json2 = it.next();
                    list.add(jsonStrToMap(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    /**
     * 将 key=value&key=value格式的 String 转为 map
     */
    public static HashMap<String,String> strToMap(String str){
        String[] strs = str.split("&");
        HashMap<String, String> m = new HashMap<String, String>();
        for(String s:strs){
            String[] ms = s.split("=");
            if (ms.length==1){
                m.put(ms[0], "");
            }else if (ms.length==2){
                m.put(ms[0], ms[1]);
            }

        }
        return m;
    }

    /**
     * 将map 转为 key=value&key=value格式的 string
     **/
    public static String mapToStr(Map<String, Object> map){
        if (map.size() == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(),"UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.substring(0,sb.length()-1);
    }

    /**
     * 将JSONObejct转换为 HashMap
     */
    public static HashMap<String,Object> jsonObjectToHashMap(JSONObject jsonObject){
        Iterator<String> it = jsonObject.keySet().iterator();
        HashMap<String,Object> map = new HashMap<>();
        while (it.hasNext()){
            String key = it.next();
            map.put(key,jsonObject.get(key));
        }
        return map;
    }


    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 驼峰转下划线
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
