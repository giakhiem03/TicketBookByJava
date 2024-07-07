package com.example.Project.controller;

import com.example.Project.model.Role;
import com.example.Project.model.User;
import com.example.Project.repository.RoleRepository;
import com.example.Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/profile")
    public String Update(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        return "user/profile";
    }

    @PostMapping("/update")
    public String Update(@ModelAttribute User user, MultipartFile imageUser, String imageDefault) {
        if (!imageUser.isEmpty()) {
            try {
                Path path = Paths.get("static/images");
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
                String fileName = UUID.randomUUID().toString() + "_" + imageUser.getOriginalFilename();
                user.setImage(fileName);
                Path pathImage = path.resolve(fileName);
                Files.copy(imageUser.getInputStream(), pathImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            user.setImage(imageDefault);
        }
        Set<Role> roleSet = new HashSet<>();
        Role role = roleRepository.findById(2).orElseThrow();
        roleSet.add(role);
        user.setRoles(roleSet);
        userService.save(user);
        return "redirect:/user/profile";
    }

    @PostMapping("/admin/update")
    public String UpdateAdmin(@ModelAttribute User user, MultipartFile imageUser, String imageDefault) {
        if (!imageUser.isEmpty()) {
            try {
                Path path = Paths.get("static/images");
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
                String fileName = UUID.randomUUID().toString() + "_" + imageUser.getOriginalFilename();
                user.setImage(fileName);
                Path pathImage = path.resolve(fileName);
                Files.copy(imageUser.getInputStream(), pathImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            user.setImage(imageDefault);
        }
        Set<Role> roleSet = new HashSet<>();
        Role role = roleRepository.findById(1).orElseThrow();
        roleSet.add(role);
        user.setRoles(roleSet);
        userService.save(user);
        return "redirect:/user/profile";
    }
}