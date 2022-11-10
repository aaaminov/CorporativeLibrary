package com.aminov.corporativelibrary.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
// @RequestMapping("/")
public class MainController {

    public MainController() { }
    
    // главная страница
    @GetMapping("/")
    public String main(Model model){
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String home(Model model){
        return "home";
    }
    
    @GetMapping("/login")
    public String login(Model model){
        return "/login";
    }
    
    // после авторизации
    @PostMapping("/login")
    public String logined(Model model){
        return "redirect:/home";
    }
    
    // после выхода пользователя
    @PostMapping("/logout")
    public String logouted(Model model){
        return "redirect:/home";
    }
    
    // @PreAuthorize("hasAnyAuthority('reader', 'manager')")
    // @GetMapping("/user")
    // @ResponseBody
    // public String user(Model model){
    //     return "hello user";
    // }
    
    // @PreAuthorize("hasAuthority('manager')")
    // @GetMapping("/admin")
    // @ResponseBody
    // public String admin(Model model){
    //     return "hello admin";
    // }
    
}
