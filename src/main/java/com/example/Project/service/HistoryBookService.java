package com.example.Project.service;

import com.example.Project.model.HistoryBook;
import com.example.Project.repository.HistoryBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryBookService {
    @Autowired
    HistoryBookRepository historyBookRepository;
    public List<HistoryBook> getAll(){
        return historyBookRepository.findAll();
    }
    public Page<HistoryBook> getAllByUsername(String userName,int page,int size){
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by("id").descending());
        return historyBookRepository.getAllByUser_userName(userName,pageRequest);
    }
    public void save(HistoryBook historyBook){
        historyBookRepository.save(historyBook);
    }
}
