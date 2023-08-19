package com.example.exampleproject;

import com.example.exampleproject.entity.Student;
import com.example.exampleproject.repository.StudentRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Component
public class DbReader implements ItemReader<List<Student>> {

    @Autowired
    StudentRepository studentRepository;

    @Override
    public List<Student> read()  {
        List<Student> students = studentRepository.findFirst10ByStatusOrderByIdDesc(false);
        if(students == null || students.size()==0){
            return null;
        }
        return students;
    }
}
