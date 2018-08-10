package com.example.resource;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class TodoResourceApplication {

  public static void main(String[] args) {
    SpringApplication.run(TodoResourceApplication.class, args);
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
}
