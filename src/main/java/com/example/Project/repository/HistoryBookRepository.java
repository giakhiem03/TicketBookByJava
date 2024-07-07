package com.example.Project.repository;

import com.example.Project.model.HistoryBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryBookRepository extends JpaRepository<HistoryBook,Integer> {
    Page<HistoryBook> getAllByUser_userName(String user_userName, PageRequest pageRequest);
}
