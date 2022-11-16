package com.aminov.corporativelibrary.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aminov.corporativelibrary.models.User;
import com.aminov.corporativelibrary.models.UserBook;
import com.aminov.corporativelibrary.repositories.RoleRepository;
import com.aminov.corporativelibrary.repositories.UserBookRepository;
import com.aminov.corporativelibrary.repositories.UserRepository;

@Controller
// @RequestMapping("/")
public class MainController {

    private final UserRepository userRepository;
    private final UserBookRepository userBookRepository;
    private final RoleRepository roleRepository;


    public MainController(UserRepository userRepository, UserBookRepository userBookRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userBookRepository = userBookRepository;
        this.roleRepository = roleRepository;
    }

    
    // главная страница
    @GetMapping("/")
    public String main(Model model){
        return "redirect:/home";
    }
    
    @GetMapping("/home")
    public String home(Model model){
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByLogin(currentPrincipalName);
        if (user == null){
            model.addAttribute("isAuthorized", false);
        } else {
            model.addAttribute("isAuthorized", true);
            model.addAttribute("role_name", user.getRole().getName());
            
            List<UserBook> allUserBooks = userBookRepository.findListByUserId(user.getId());
            List<UserBook> userBooks = new ArrayList<>();
            Date today = Calendar.getInstance().getTime();
            for (UserBook userBook : allUserBooks) {
                if (userBook.getReturn_date().getTime() != 0L
                && (userBook.getIssue_date().compareTo(today)) <= 0 && (userBook.getReturn_date().compareTo(today)) >= 1) { // если это текущая аренда
                    userBooks.add(userBook);
            }
            }
            model.addAttribute("userBooks", userBooks);
        }
        return "home";
    }
    
    @GetMapping("/login")
    public String login(Model model){
        return "/login";
    }
    
    // // после авторизации
    // @PostMapping("/login")
    // public String logined(Model model){
    //     return "redirect:/home";
    // }
    
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

    // форма регистрации
    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "/register";
    }
    
    // регистрация
    @PostMapping("/register")
    public String newUser(@ModelAttribute("user") User user){
        if (user == null){
            return null;
        }
        if (userRepository.findByLogin(user.getLogin()) != null){
            return "redirect:/register?login_exists";
        }
        if (user.getLogin() == "" || user.getPassword() == ""){
            return "redirect:/register?error";
        }
        user.setRole(roleRepository.findById(1L).get()); // reader
        user.setForeign_id(0L);
        userRepository.save(user);
        return "redirect:/home";
    }
    
    
    
}
