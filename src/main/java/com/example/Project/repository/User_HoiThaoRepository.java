package com.example.Project.repository;

import com.example.Project.model.User_HoiThao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User_HoiThaoRepository extends JpaRepository<User_HoiThao,Integer> {

}
