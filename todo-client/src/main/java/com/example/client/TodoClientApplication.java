package com.example.client;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

@SpringBootApplication
@EnableOAuth2Sso
public class TodoClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(TodoClientApplication.class, args);
  }

  @Configuration
  public class MappingConfiguration {

    @Bean
    public DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean(
        @Value("classpath*:mappings/*mappings.xml") Resource[] resources) throws Exception {
      final DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean =
          new DozerBeanMapperFactoryBean();
      dozerBeanMapperFactoryBean.setMappingFiles(resources);
      return dozerBeanMapperFactoryBean;
    }
  }

  @Bean
  OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
      OAuth2ProtectedResourceDetails details) {
    return new OAuth2RestTemplate(details, oauth2ClientContext);
  }
}
