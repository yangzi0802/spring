package com.shiner.test.controller;

import com.shiner.spring.annotation.Autowried;
import com.shiner.spring.annotation.Controller;
import com.shiner.spring.annotation.RequestMapping;
import com.shiner.test.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowried("userService")
    private UserService userService;

    @RequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response ,String name){
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.write("name = " + name);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(pw != null) {
                pw.close();
            }
        }
    }
}
