package com.example.exampleproject.batch;

import com.example.exampleproject.entity.Student;
import com.example.exampleproject.repository.StudentJpaRepository;
import com.example.exampleproject.repository.StudentPandSRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class JobConfig {

    @Autowired
    private StudentJpaRepository studentJpaRepository;
    @Autowired
    private StudentPandSRepository repository;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

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
                .<Student, Student> chunk(20)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Tasklet dataPrepareTasklet(){
        return (contribution, chunkContext) -> {
            for(int i = 0; i<100; i++){
                Student student = studentJpaRepository.save(new Student(i, "Student"+i));
            }
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public ItemReader<Student> reader() {
        int maxId = 45;

        RepositoryItemReader<Student> reader = new RepositoryItemReader<>();
        reader.setRepository(repository);
        reader.setMethodName("findByIdLessThan");
        reader.setArguments(Collections.singletonList(maxId));
        reader.setPageSize(20);

        HashMap<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        reader.setSort(sorts);

        return reader;
    }

    @Bean
    public ItemProcessor<Student,Student> processor(){
        return item -> {
            item.setName("Cool_" + item.getName());
            return item;
        };
    }

    @Bean
    public ItemWriter<Student> writer(){
        return items -> {
            //items.forEach(System.out::println);
            repository.saveAll(items);
            System.out.println("Done for "+ items.size()+ " elements");
        };
    }


}
