package com.example.heroku;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
public class HerokuApplication {


  public static void main(String[] args) throws Exception {
    SpringApplication.run(HerokuApplication.class, args);
  }
}
