package com.example.Project.service;

import com.example.Project.model.User_HoiThao;
import com.example.Project.repository.User_HoiThaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class User_HoiThaoService {
    @Autowired
    User_HoiThaoRepository userHoiThaoRepository;
    public void save(User_HoiThao userHoiThao){
        userHoiThaoRepository.save(userHoiThao);
    }
    public Page<User_HoiThao> getAll(int page,int size){
        PageRequest pageRequest = PageRequest.of(page,size,Sort.by("id").descending());
        return userHoiThaoRepository.findAll(pageRequest);
    }
}
