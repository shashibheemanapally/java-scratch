package com.example.exampleproject.batch;

import com.example.exampleproject.DbReader;
import com.example.exampleproject.DbWriter;
import com.example.exampleproject.entity.Student;
import com.example.exampleproject.repository.StudentRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableBatchProcessing
public class JobConfig {
    @Autowired
    private StudentRepository repository;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private DbReader dbReader;

    @Autowired
    private DbWriter dbWriter;

@Bean
public Job job(){
    return jobBuilderFactory.get("job")
            .incrementer(new RunIdIncrementer())
            .start(step1())
            .next(step2())
            .build();
}



    @Bean
    public Step step1(){
        return stepBuilderFactory.get("step1")
                .tasklet(dataPrepareTasklet())
                .build();
    }


@Bean
public Step step2(){
    return stepBuilderFactory.get("step2")
            .<List<Student>, List<Student>> chunk(1)
            .reader(dbReader)
            .writer(dbWriter)
            .build();
}

    @Bean
    public Tasklet dataPrepareTasklet(){
        return (contribution, chunkContext) -> {
            for(int i = 0; i<43; i++){
                Student student = repository.save(new Student(i, "Student"+i, false));
            }
            for(int i = 43; i<100; i++){
                Student student = repository.save(new Student(i, "Student"+i, true));
            }
            return RepeatStatus.FINISHED;
        };
    }
    

}
