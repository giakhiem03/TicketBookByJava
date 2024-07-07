package com.example.Project.controller;

import com.example.Project.model.Role;
import com.example.Project.model.User;
import com.example.Project.repository.RoleRepository;
import com.example.Project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/login")
    public String login(Model model){
        return "/user/login";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user",new User());
        return "/user/register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user, BindingResult result){
        if(result.hasErrors()){
            return "/user/register";
        }
        Set<Role> role = new HashSet<>();
        for(var item : roleRepository.findAll()){
            if(item.getRoleId()==2){
                role.add(item);
            }
        }
        user.setRoles(role);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login";
    }

}
