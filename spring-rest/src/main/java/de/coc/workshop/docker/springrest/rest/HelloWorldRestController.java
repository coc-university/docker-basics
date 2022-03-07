package de.coc.workshop.docker.springrest.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldRestController {

    @Value("${helloValue}")
    private String helloValue;

    @GetMapping("/hello")
    public String getHelloWorld(){
      return String.format("Hello %s!", helloValue);
    }
}
