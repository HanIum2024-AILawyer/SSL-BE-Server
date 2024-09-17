package com.lawProject.SSL.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "main";
    }

    @GetMapping("/home")
    public String home() {
        return "home - 로그인 필요";
    }

    @GetMapping("/healthcheck")
    public String healthcheck() {
        return "OK";
    }
}
