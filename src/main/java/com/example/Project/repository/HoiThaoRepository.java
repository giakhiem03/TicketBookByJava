package com.example.Project.repository;

import com.example.Project.model.HoiThao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface HoiThaoRepository extends JpaRepository<HoiThao,Integer> {
    Page<HoiThao> findByTimeAfter(LocalDateTime time, Pageable pageable);
    @Query("SELECT p FROM HoiThao p WHERE p.title like %:title%")
    Page<HoiThao> searchHoiThaos(@Param("title") String title, Pageable pageable);
    Page<HoiThao> findByCategory_IdAndPriceEquals(int categoryId, double price, Pageable pageable);
    Page<HoiThao> findByCategory_IdAndPriceNot(int categoryId, double price, Pageable pageable);
    Page<HoiThao> findByCategoryId(int categoryId, Pageable pageable);
}
