package com.example.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dorofeev
 */
@RestController
public class TestController {

    @Value("${custom.name:#{null}}")
    protected String customName;

    @RequestMapping("/")
    public String home() {
        return "Hello World! Prop 'custom.name': " + customName;
    }
}
