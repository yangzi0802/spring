package com.shiner.spring.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class RequestParamUtils {

    /**
     * 解析请求的参数类型
     * @return
     */
    public static Object[] resolveRequestParam(Method method, HttpServletRequest request, HttpServletResponse response){
        //获取请求的参数列表
        Map<String, String[]> parameterMap = request.getParameterMap();

        //获取方法中参数的类型
        Class<?>[] parameterTypes = method.getParameterTypes();

        //方法的参数列表
        Object [] paramValues= new Object[parameterTypes.length];
        for (int i = 0; i<parameterTypes.length; i++){
            //根据参数名称，做某些处理
            String requestParam = parameterTypes[i].getSimpleName();
            if (requestParam.equals("HttpServletRequest")){
                //参数类型已明确，这边强转类型
                paramValues[i]=request;
                continue;
            }
            if (requestParam.equals("HttpServletResponse")){
                paramValues[i]=response;
                continue;
            }
            if(requestParam.equals("String")){
                for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
                    String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
                    paramValues[i]=value;
                }
            }
        }
        return paramValues;
    }
}
