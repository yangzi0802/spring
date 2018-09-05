package com.shiner.spring.servlet;

import com.shiner.spring.annotation.*;
import com.shiner.spring.util.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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

    /**URL映射关系*/
    private Map<String,Object> handleMapping = new HashMap<String,Object>();

    @Override
    public void init(ServletConfig config) throws ServletException {

        //1、扫描指定包下需要实例化的类
        doScan("com.shiner");
        for(String classPath : classPaths){
            System.out.println(classPath);
        }

        //2、初始化所有扫描到的类
        doInstance();

        //3、依赖注入
        doDependency();

        //4、映射关系
        doHandleMapping();
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
                    String key = "".equals(controller.value()) ? StringUtils.classNameSplit(realClassPath) : controller.value();
                    beansMap.put(key,controller);
                }else if(clazz.isAnnotationPresent(Autowried.class)){
                    Autowried autowried = (Autowried) clazz.newInstance();
                    String key = "".equals(autowried.value()) ? StringUtils.classNameSplit(realClassPath) : autowried.value();
                    beansMap.put(key,autowried);
                }else if(clazz.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping requestMapping = (RequestMapping) clazz.newInstance();
                    String key = "".equals(requestMapping.value()) ? StringUtils.classNameSplit(realClassPath) : requestMapping.value();
                    beansMap.put(key,requestMapping);
                }else if(clazz.isAnnotationPresent(Service.class)){
                    Service service = (Service) clazz.newInstance();
                    String key = "".equals(service.value()) ? StringUtils.classNameSplit(realClassPath) : service.value();
                    beansMap.put(key,service);
                }else if(clazz.isAnnotationPresent(RequestParam.class)){
                    RequestParam requestParam = (RequestParam) clazz.newInstance();
                    String key = "".equals(requestParam.value()) ? StringUtils.classNameSplit(realClassPath) : requestParam.value();
                    beansMap.put(key,requestParam);
                }else {
                    continue;
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

    /**依赖注入*/
    private void doDependency() {
        if(beansMap.isEmpty()) return;
        //循环遍历beansMap
        for(Object bean : beansMap.values()){
            Class<?> clazz = bean.getClass();
            //如果当前bean被Controller 声明 需要得到所有的字段 判断字段上的注解
            if(clazz.isAnnotationPresent(Controller.class)){
                Field[] fields = clazz.getDeclaredFields();
                for(Field field : fields){
                    if(field.isAnnotationPresent(Autowried.class)){
                        Autowried autowried = field.getAnnotation(Autowried.class);
                        String key = autowried.value();
                        //1、通过key 去实例化好的beansMap 里取bean
                        //2、为了防止字段是private修饰符修饰 需要将其字段的可见性设为true
                        field.setAccessible(true);
                        try {
                            field.set(clazz,beansMap.get(key));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else if(clazz.isAnnotationPresent(Service.class)){
                Field[] fields = clazz.getDeclaredFields();
                for(Field field : fields){
                    if(field.isAnnotationPresent(Service.class)){
                        Service service = field.getAnnotation(Service.class);
                        String key = service.value();
                        //1、通过key 去实例化好的beansMap 里取bean
                        //2、为了防止字段是private修饰符修饰 需要将其字段的可见性设为true
                        field.setAccessible(true);
                        try {
                            field.set(clazz,beansMap.get(key));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                continue;
            }
        }
    }

    /**映射关系*/
    private void doHandleMapping() {
        if(beansMap.isEmpty()) return;
        for(Object bean : beansMap.values()){
            Class<?> clazz = bean.getClass();
            if(clazz.isAnnotationPresent(Controller.class)){
                if(clazz.isAnnotationPresent(RequestMapping.class)){

                }
            }

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
