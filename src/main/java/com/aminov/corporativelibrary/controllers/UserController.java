package com.aminov.corporativelibrary.controllers;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aminov.corporativelibrary.models.Role;
import com.aminov.corporativelibrary.models.User;
import com.aminov.corporativelibrary.repositories.RoleRepository;
import com.aminov.corporativelibrary.repositories.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository repository;
    private final RoleRepository roleRepository;

    public UserController(UserRepository repository, RoleRepository roleRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    // получение всех пользователей
    @GetMapping()
    public String allUsers(Model model){
        model.addAttribute("users", repository.findAll());
        return "user/all";
    }
    
    // получение одного юзера
    @GetMapping("/{id}")
    public String oneUser(Model model, @PathVariable("id") Long id){
        model.addAttribute("user", repository.findById(id).get());
        return "user/one";
    }

    // получение формы для редактирования юзера
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('manager')")
    public String editUser(Model model, @PathVariable("id") Long id){
        List<Role> roleListAll = roleRepository.findAll();
        model.addAttribute("user", repository.findById(id).get());
        model.addAttribute("roleListAll", roleListAll);
        return "user/edit";
    }

    // обновление юзера
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('manager')")
    public String updateUser( // @ModelAttribute("user") User user, 
        @PathVariable("id") Long id, @PathParam("role_id") Long role_id, @PathParam("user_id") Long user_id){
        User user = repository.findById(user_id).get();
        user.setRole(roleRepository.findById(role_id).get());
        repository.save(user);
        return "redirect:/user/{id}";
    }

    
}
