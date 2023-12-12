package com.example.exampleproject.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class MyTasklet implements Tasklet {
    String[] elements = {"element1", "element2", "element3", "element4"};

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext jobContext = contribution
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext();
        jobContext.put("elements",elements);
        jobContext.putInt("pointer", 0);
        return RepeatStatus.FINISHED;
    }
}