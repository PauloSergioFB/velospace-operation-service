package br.com.fiap.javaadv.VeloSpace.presentation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeApiController {

    @GetMapping("/")
    public String redirectToDocs() {
        return "redirect:/swagger-ui/index.html";
    }

}
