package com.example.resource.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

  static final String RESOURCE_ID = "resourceA";

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .antMatchers(HttpMethod.GET, "/api/v1/**").access("#oauth2.hasScope('read')")
      .antMatchers(HttpMethod.POST, "/api/v1/**").access("#oauth2.hasScope('write')")
      .antMatchers(HttpMethod.PUT, "/api/v1/**").access("#oauth2.hasScope('write')")
      .antMatchers(HttpMethod.DELETE, "/api/v1/**").access("#oauth2.hasScope('delete')");
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) {
    resources.resourceId(RESOURCE_ID);
  }
}
