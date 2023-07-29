package com.example.exampleproject.repository;

import com.example.exampleproject.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentPandSRepository extends PagingAndSortingRepository<Student, Integer> {
    Page<Student> findByIdLessThanOrderByIdDesc(int id, Pageable pageable);
    Page<Student> findByIdLessThan(int id, Pageable pageable);
}
