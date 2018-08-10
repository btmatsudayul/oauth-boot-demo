package com.example.authz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthnController {

  @GetMapping("/")
  String index() {
    return "index";
  }

  @GetMapping("login")
  String login() {
    return "login";
  }

//  @GetMapping("me")
  
}
