package com.example.exampleproject.batch;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobConfig {

    @Autowired
    MyTasklet myTasklet;

    @Autowired
    MyStepListener myStepListener;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    PlatformTransactionManager transactionManager;



    public Step step1(){
        return new StepBuilder( "step1", jobRepository)
                .tasklet(myTasklet, transactionManager)
                .build();
    }
    public Step step2(){
        return new StepBuilder( "step2", jobRepository)
                .<Person,Person>chunk(2,transactionManager)
                .reader(reader())
                .writer(writer())
                .listener(myStepListener)
                .build();
    }

    @Bean
    public Job job(){
        SimpleJobBuilder builder = new JobBuilder("job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1());

        attachFlows(builder, 5);
        return builder.build();

    }

    private void attachFlows(SimpleJobBuilder builder, int n){
        builder.next(step2());
        FlowBuilder<FlowJobBuilder> flowBuilder = builder.on("CONTINUE").to(step2()).on("COMPLETED").end();
        for(int i = 1; i<n; i++){
            flowBuilder.on("CONTINUE").to(step2()).on("COMPLETED").end();
        }
    }

    @Bean
    public FlatFileItemReader<Person> reader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("input.csv"));
        reader.setLineMapper(lineMapper());
        return reader;
    }

    @Bean
    public LineMapper<Person> lineMapper() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id", "firstName", "lastName");

        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Person.class);

        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }


    @Bean
    public ItemWriter<Person> writer(){
        return chunk -> System.out.println(chunk.toString());
    }



}
