package com.example.jwttest.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.jwttest.models.User;
import com.example.jwttest.services.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;

//import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin

public class UserController {
    private final UserService userService;


    @GetMapping("/user")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    @GetMapping("/test")
    public String getMethodName() {
        return "hello from users";
    }
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        //return ResponseEntity.ok().body(userService.getUsers());
        return new ResponseEntity<>(userService.getAll(),HttpStatus.OK);
    }
    
    @PostMapping("/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user),HttpStatus.CREATED);
    }
    
    @PostMapping("path")
    public String postMethodName(@RequestBody String entity) {
        return entity;
    }
    
    
}
