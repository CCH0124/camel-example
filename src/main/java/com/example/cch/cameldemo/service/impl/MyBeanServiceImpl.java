package com.example.cch.cameldemo.service.impl;

import com.example.cch.cameldemo.service.MyBeanService;

import org.springframework.stereotype.Service;

@Service
public class MyBeanServiceImpl implements MyBeanService {

    @Override
    public String saySomething() {
        // TODO Auto-generated method stub
        return "Hello Camel foo";
    }
    
}
