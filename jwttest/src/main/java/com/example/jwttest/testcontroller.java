package com.example.jwttest;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class testcontroller {
    @GetMapping("/")
    public String getMethodName() {
        return "hello spring";
    }
    @GetMapping("/tv")
    public String gettv() {
        return "deadwood";
    }
    
    
}
