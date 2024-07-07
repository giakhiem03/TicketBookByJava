package com.example.Project.service;

import com.example.Project.model.HoiThao;
import com.example.Project.repository.HoiThaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HoiThaoService {
    @Autowired
    HoiThaoRepository hoiThaoRepository;
    public Page<HoiThao> getAll(int pageNo, int pageSize){
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize,Sort.by("time").descending());
        return hoiThaoRepository.findAll(pageRequest);
    }
    public Page<HoiThao> getAllByCategory(int categoryId,int pageNo, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("time").descending());
        return hoiThaoRepository.findByCategoryId(categoryId, pageRequest);
    }
    public void save(HoiThao hoiThao){
        hoiThaoRepository.save(hoiThao);
    }
    public HoiThao getById(int id){
        return hoiThaoRepository.findById(id).orElseThrow();
    }
    public Page<HoiThao> getAllforHome(int pageNo, int pageSize){
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize,Sort.by("time").descending());
        LocalDateTime timeThreshold = LocalDateTime.now().minusDays(5);
        return hoiThaoRepository.findByTimeAfter(timeThreshold, pageRequest);
    }

    public Page<HoiThao> searchByTitle(String title,int pageNo, int pageSize){
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize,Sort.by("time").descending());
        return hoiThaoRepository.searchHoiThaos(title,pageRequest);
    }
    public Page<HoiThao> findByCategoryAndPriceEquals(int categoryId, double price, int pageNo, int pageSize) {
        return hoiThaoRepository.findByCategory_IdAndPriceEquals(categoryId, price, PageRequest.of(pageNo, pageSize,Sort.by("time").descending()));
    }

    public Page<HoiThao> findByCategoryAndPriceNotEquals(int categoryId, double price, int pageNo, int pageSize) {
        return hoiThaoRepository.findByCategory_IdAndPriceNot(categoryId, price, PageRequest.of(pageNo, pageSize,Sort.by("time").descending()));
    }
    public void delete(HoiThao hoiThao){
        hoiThaoRepository.delete(hoiThao);
    }
}
