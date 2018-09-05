package com.shiner.spring.servlet;

import com.shiner.spring.annotation.Controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatchServlet extends HttpServlet {
    /**存储找到class的类路径，用于初始化类*/
    private List<String> classPaths = new ArrayList<String>();

    /**存储实例化后的bean key默认为类名 首字母小写，value为实例化后的对象*/
    private Map<String,Object> beansMap = new HashMap<String,Object>();

    @Override
    public void init(ServletConfig config) throws ServletException {

        //1、扫描指定包下需要实例化的类
        doScan("com.shiner");
        for(String classPath : classPaths){
            System.out.println(classPath);
        }

        //2、初始化所有扫描到的类
        doInstance();
    }



    /**扫描class类*/
    private void doScan(String basePackage) {
        //扫描编译完成项目路径下的所有类
        URL url = this.getClass().getClassLoader().getResource("/" + basePackage.replaceAll("\\.","/"));
        //获取根目录
        String baseFile = url.getFile();
        File createFile = new File(baseFile);
        //获取根目录下的所有文件的路径
        String[] filesPath = createFile.list();
        for(String filePath : filesPath){
            File realPath = new File(baseFile + filePath);
            //如果是目录 递归去找 直至找到class类
            if(realPath.isDirectory()){
                doScan(basePackage + "." + filePath);
            }else{
                //找到class文件类路径 将类路径放到list集合中。
                //例如：com/shiner/spring/controller/UserController.class
                classPaths.add(basePackage + "." + realPath.getName());
            }
        }
    }

    /**初始化类*/
    private void doInstance() {
        if(classPaths.isEmpty()) return;
        //遍历扫描到的类路径，通过反射将类实例化
        for(String fullPath : classPaths){
            String realClassPath = fullPath.replaceAll(".class","");
            try {
                Class<?> clazz = Class.forName(realClassPath);
                if(clazz.isAnnotationPresent(Controller.class)){
                    Controller controller = (Controller) clazz.newInstance();
                    //beansMap.put();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
