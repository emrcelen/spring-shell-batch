package com.wenthor.batchJShell.configuration;

import com.wenthor.batchJShell.batch.CustomMapSqlParameterSourceProvider;
import com.wenthor.batchJShell.batch.JobCompletionNotificationListener;
import com.wenthor.batchJShell.batch.ReadListener;
import com.wenthor.batchJShell.service.BatchService;
import com.wenthor.batchJShell.service.rabbitmq.producer.NotificationProducer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Lazy
@Configuration
public class BatchConfiguration {
    private List<String> headers = new ArrayList<>();
    private List<String> columnsDataTypes = new ArrayList<>();
    private String delimiter = ";";
    private String fileName = "";
    private String insertQuery = "";
    private DataSource dataSource;

    private final BatchService batchService;
    private final JobRepository jobRepository;

    public BatchConfiguration(BatchService batchService, JobRepository jobRepository) {
        this.batchService = batchService;
        this.jobRepository = jobRepository;
        this.headers = this.batchService.getHeaders();
        this.columnsDataTypes = this.batchService.getcolumnsDataTypes();
        this.delimiter = this.batchService.getDelimiter();
        this.fileName = this.batchService.getFileName();
        this.insertQuery = this.batchService.getInsertQuery();
        this.dataSource = this.batchService.getDataSource();
    }

    @Bean
    public Job createJob(Step step, NotificationProducer notificationProducer){
        return new JobBuilder("job", this.jobRepository)
                .listener(new JobCompletionNotificationListener(batchService, notificationProducer))
                .flow(step)
                .end().build();
    }

    @Bean
    public Step createStep(
            ItemReader<Map<Object, Object>> reader,
            ItemWriter<Map<String,Object>> writer,
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager){
        return new StepBuilder("step", jobRepository)
                .<Map<Object, Object>,Map<String, Object>>chunk(2,transactionManager)
                .allowStartIfComplete(true)
                .reader(reader)
                .listener(new ReadListener())
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<Map<Object, Object>> csvFileReader(LineMapper<Map<Object, Object>> lineMapper){
        var itemReader = new FlatFileItemReader<Map<Object, Object>>();
        itemReader.setLineMapper(lineMapper);
        itemReader.setLinesToSkip(1);
        itemReader.setResource(new ClassPathResource("csv/".concat(getFileName()).concat(".csv")));
        return itemReader;
    }


    @Bean
    public DefaultLineMapper<Map<Object, Object>> lineMapper(LineTokenizer lineTokenizer, FieldSetMapper<Map<Object, Object>> fieldSetMapper){
        var lineMapper = new DefaultLineMapper<Map<Object, Object>>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public FieldSetMapper<Map<Object, Object>> fieldSetMapper(){
        return fieldSet -> {
            Map<Object, Object> map = new HashMap<>();
            String[] names = fieldSet.getNames();
            for(int i = 0; i < names.length; i++){
                final String columnType = this.columnsDataTypes.get(i);
                if(columnType.contains("bit") || columnType.contains("int"))
                    map.put(names[i],fieldSet.readInt(names[i]));
                else if(columnType.contains("decimal") || columnType.contains("numeric") ||
                columnType.contains("money"))
                    map.put(names[i],fieldSet.readDouble(names[i]));
                else if(columnType.contains("date") || columnType.contains("time"))
                    map.put(names[i], fieldSet.readDate(names[i]));
                else if(columnType.contains("bool"))
                    map.put(names[i], fieldSet.readBoolean(names[i]));
                else
                    map.put(names[i], fieldSet.readString(names[i]));
            }
            return map;
        };
    }
    @Bean
    public DelimitedLineTokenizer tokenizer(){
        var tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(getDelimiter());
        tokenizer.setNames(getHeaders().stream().toArray(String[]::new));
        return tokenizer;
    }
    @Bean
    public JdbcBatchItemWriter<Map<String, Object>> jdbcBatchItemWriter(){
        var provider = new CustomMapSqlParameterSourceProvider();
        var itemWriter = new JdbcBatchItemWriter<Map<String, Object>>();
        itemWriter.setDataSource(getDataSource());
        itemWriter.setSql(getInsertQuery());
        itemWriter.setJdbcTemplate(new NamedParameterJdbcTemplate(getDataSource()));
        itemWriter.setItemSqlParameterSourceProvider(provider);
        return itemWriter;
    }

    // Getter & Setter:
    public List<String> getHeaders() {
        return headers;
    }
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public String getDelimiter() {
        return delimiter;
    }
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInsertQuery() {
        return insertQuery;
    }
    public void setInsertQuery(String insertQuery) {
        this.insertQuery = insertQuery;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}
