package com.challnege.delivery.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Thymeleaf 템플릿 이름
    }
}
