package com.personal.springboot.controller.base;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 异常ERROR页面定义
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月6日
 */
@Controller
@RequestMapping("/")
public class MyErrorController implements ErrorController {

    public static final String ERROR_PATH = "error2/";

    @RequestMapping(ERROR_PATH)
    public String handleError() {
        return ERROR_PATH + "error";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

}