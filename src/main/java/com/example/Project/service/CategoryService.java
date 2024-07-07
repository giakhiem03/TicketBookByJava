package com.example.Project.service;

import com.example.Project.model.CategoryHoiThao;
import com.example.Project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public List<CategoryHoiThao> getAll(){
        return categoryRepository.findAll();
    }
}
