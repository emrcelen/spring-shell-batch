package com.wenthor.batchJShell.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Service
public class BatchService {
    private String delimiter = ";";
    private String fileName = "";
    private String insertQuery = "";
    private String mail = "";

    private final Job job;
    private final JobLauncher jobLauncher;
    private final DatabaseService databaseService;

    public BatchService(@Lazy Job job, JobLauncher jobLauncher, DatabaseService databaseService) {
        this.job = job;
        this.jobLauncher = jobLauncher;
        this.databaseService = databaseService;
    }
    public String  selectFileAndDelimiter(String delimiter, String fileName, String mail){
        this.delimiter = delimiter;
        this.fileName = fileName;
        this.mail = mail;
        updateInsertQuery();
        return "Delimiter: '".concat(this.getDelimiter()).concat("'")
                .concat(" File Name: '").concat(this.getFileName()).concat("'")
                .concat(" Mail: '".concat(this.getMail()).concat("'"));
    }
    public void batchStarter(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        JobExecution jobExecution = null;
        try {
            jobExecution = jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            System.err.println("Job Execution Already Running Exception!");
        } catch (JobRestartException e) {
            System.err.println("Job Restart Exception.");
        } catch (JobInstanceAlreadyCompleteException e) {
            System.err.println("JobInstanceAlreadyCompleteException");
        } catch (JobParametersInvalidException e) {
            System.err.println("JobParametersInvalidException");
        }
        var batchStatus = jobExecution.getStatus();
        while (batchStatus.isRunning()) {
            System.out.println("Still running...");
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
                System.err.println("Thread exception.");
            }
        }
    }


    public String getDelimiter() {
        return delimiter;
    }
    public String getMail() {
        return mail;
    }
    public String getFileName() {
        return fileName;
    }
    public String getInsertQuery() {
        return insertQuery;
    }
    public List<String> getHeaders(){return this.databaseService.getColumns();}
    public List<String>  getcolumnsDataTypes(){return this.databaseService.getColumnsDataTypes();}
    public DataSource getDataSource(){return this.databaseService.getDataSource();}
    private void updateInsertQuery(){
        this.insertQuery = this.databaseService.createInsertSQL();
    }

}
