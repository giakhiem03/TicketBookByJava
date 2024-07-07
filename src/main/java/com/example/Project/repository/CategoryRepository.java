package com.example.Project.repository;

import com.example.Project.model.CategoryHoiThao;
import com.example.Project.model.HoiThao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryHoiThao,Integer> {

}
