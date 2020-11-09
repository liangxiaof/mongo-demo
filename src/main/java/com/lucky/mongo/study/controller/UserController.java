package com.lucky.mongo.study.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: liangtf
 * @description:
 * @date: 2020/11/5 18:27
 */
@RestController
public class UserController {

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

}
