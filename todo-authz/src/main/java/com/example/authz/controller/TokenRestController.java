package com.example.authz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/revoke")
public class TokenRestController {

  @Autowired
  ConsumerTokenServices tokenService;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void revokeToken(@RequestParam("token") String tokenValue,
      @RequestParam("token_type_hint") String tokenType) {
    
    tokenService.revokeToken(tokenValue);
  }
}
