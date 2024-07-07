package com.example.Project.repository;

import com.example.Project.model.User_GiaiDau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User_GiaiDauRepository extends JpaRepository<User_GiaiDau,Integer> {

}
