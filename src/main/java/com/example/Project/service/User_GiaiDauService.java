package com.example.Project.service;

import com.example.Project.model.User_GiaiDau;
import com.example.Project.model.User_HoiThao;
import com.example.Project.repository.User_GiaiDauRepository;
import com.example.Project.repository.User_HoiThaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class User_GiaiDauService {
    @Autowired
    User_GiaiDauRepository userGiaiDauRepository;
    public void save(User_GiaiDau userGiaiDau){
        userGiaiDauRepository.save(userGiaiDau);
    }
    public Page<User_GiaiDau> getAll(int page,int size){
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("id").descending());
        return userGiaiDauRepository.findAll(pageRequest);
    }
}
