package com.example.exampleproject.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class MyStepListener implements StepExecutionListener {


    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
        String[] elements = (String[]) jobContext.get("elements");
        int pointer = jobContext.getInt("pointer");
        assert elements != null;
        System.out.println("Current pointer is on: " + elements[pointer]);

    }

    @Nullable
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext jobContext = stepExecution.getJobExecution().getExecutionContext();
        int newPointer = jobContext.getInt("pointer")+1;
        String[] elements = (String[]) jobContext.get("elements");
        assert elements != null;
        if(elements.length == newPointer){
            return ExitStatus.COMPLETED;
        }
        jobContext.putInt("pointer",newPointer);
        System.out.println("current pointer value: " + newPointer);
        return new ExitStatus("CONTINUE");
    }
}
