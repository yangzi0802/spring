package com.shiner.spring.util;

public class StringUtils {

    //将全类名的.class 去掉并将类名首字母变小写
    public static String classNameSplit(String className){
        if(className.contains(".class")){
            className = className.replaceAll(".class","");
        }
        className = className.substring(className.lastIndexOf(".") + 1);
        char[] chars = className.toCharArray();
        chars[0] += 32;
        return className = String.valueOf(chars);
    }

    public static void main(String[] args) {
        String str = "com.shiner.spring.Test";
        System.out.println(classNameSplit(str));
    }

}
