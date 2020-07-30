package com.studentcloud.dbaccess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Iterator;

@Controller
public class MainController {
    @GetMapping(path = "/")
    public String home(Model model){
        model.addAttribute("title", "Главная страница");
        return "home";
    }
}
