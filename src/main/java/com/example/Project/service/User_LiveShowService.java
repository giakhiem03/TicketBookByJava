package com.example.Project.service;

import com.example.Project.model.User_HoiThao;
import com.example.Project.model.User_LiveShow;
import com.example.Project.repository.User_LiveShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class User_LiveShowService {
    @Autowired
    User_LiveShowRepository userLiveShowRepository;
    public void save(User_LiveShow userLiveShow){
        userLiveShowRepository.save(userLiveShow);
    }
    public Page<User_LiveShow> getAll(int page, int size){
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("id").descending());
        return userLiveShowRepository.findAll(pageRequest);
    }
}
