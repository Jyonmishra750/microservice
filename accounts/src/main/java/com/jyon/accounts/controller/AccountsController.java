package com.jyon.accounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    @GetMapping("/say-hello")
    public String hello(){
        return "Hello!! Welcome to Microservice World.";
    }
}
