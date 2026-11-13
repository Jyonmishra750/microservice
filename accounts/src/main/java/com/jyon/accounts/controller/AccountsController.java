package com.jyon.accounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        return "Hello, "+ name + ". Welcome to the Microservice World.";
    }
}
