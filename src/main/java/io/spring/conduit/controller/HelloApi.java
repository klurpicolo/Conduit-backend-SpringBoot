package io.spring.conduit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloApi {

    @GetMapping("/hello")
    public String helloThere(){
        return "fck hello";
    }

    @GetMapping("/user")
    public String helloUser(){
        return "fck hello user ";
    }

    @GetMapping("/admin")
    public String helloAdmin(){
        return "fck hello admin";
    }
}
