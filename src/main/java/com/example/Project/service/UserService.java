package com.example.Project.service;

import com.example.Project.model.User;
import com.example.Project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public User getUserByUsername(String name){
        for(var item : userRepository.findAll()){
            if(item.getUserName().equals(name)){
                return item;
            }
        }
        return null;
    }
    public List<User> getAll(){
        return userRepository.findAll();
    }
    public void save(User user){
        userRepository.save(user);
    }

    public void update(User user){
        User user_update = userRepository.findById(user.getId()).orElseThrow(()->null);
        user_update.setUserName(user.getUserName());
        user_update.setPassword(user.getPassword());
        user_update.setRoles(user.getRoles());
        userRepository.save(user_update);
    }
    public void delete(User user){
        userRepository.delete(user);
    }
}
