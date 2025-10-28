package io.github.PercivalGebashe.portfolio_project_showcase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    @GetMapping("/")
    public String landingPage() {
        return "index";
    }
}
