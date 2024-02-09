package com.wenthor.batchJShell.service;

import com.wenthor.batchJShell.enumeration.Database;
import com.wenthor.batchJShell.model.DatabaseConnectionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DatabaseService {
    private DatabaseConnectionModel databaseConnectionModel;
    private DataSource dataSource;

    private String tableName = "";
    private String tableSchema = "";
    private List<String> columns = new ArrayList<>();
    private List<String> columnsDataTypes = new ArrayList<>();
    private StringBuilder SQL = new StringBuilder();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void createDatabaseConnectionModel(String dbUrl,
                                              String dbPort,
                                              String dbName,
                                              String dbUserName,
                                              String dbPassword,
                                              Database database){
        this.databaseConnectionModel = new DatabaseConnectionModel.Builder()
                .url(dbUrl)
                .port(dbPort)
                .name(dbName)
                .userName(dbUserName)
                .password(dbPassword)
                .database(database)
                .build();
        log.debug("DatabaseConnectionModel created.");
    }

    public void getDatabaseColumnsName(String tableName, String schema){
        this.tableName = tableName;
        this.tableSchema = schema;
        stringBuilderClear();
        SQL.append("SELECT column_name FROM information_schema.columns WHERE table_name = '")
                .append(tableName).append("' AND table_schema = '")
                .append(schema).append("'");
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(this.getDataSource());
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(SQL.toString());
        this.columns = maps.stream()
                .flatMap(map -> map.values().stream())
                .map(Object::toString)
                .collect(Collectors.toList());
        this.columns.remove(0);
        getDatabaseColumnsTypes(getTableName(),getTableSchema());
    }
    public void createDataSource(String databaseUrl){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(this.databaseConnectionModel.getDatabase().getDriver());
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(this.databaseConnectionModel.getDbUserName());
        dataSource.setPassword(this.databaseConnectionModel.getDbPassword());
        this.dataSource = dataSource;
        log.debug("DataSource created.");
    }

    private void getDatabaseColumnsTypes(String tableName, String schema){
        stringBuilderClear();
        SQL.append("SELECT data_type FROM information_schema.columns WHERE table_name = '")
                .append(tableName).append("' AND table_schema = '")
                .append(schema).append("'");
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(this.getDataSource());
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(SQL.toString());
        this.columnsDataTypes = maps.stream()
                .flatMap(map -> map.values().stream())
                .map(Object::toString)
                .collect(Collectors.toList());
        this.columnsDataTypes.remove(0);
    }

    public String createPostgreUrl(){
        StringBuilder dbURL = new StringBuilder();
        dbURL
                .append("jdbc:")
                .append(this.databaseConnectionModel.getDatabase().getName())
                .append("://")
                .append(this.databaseConnectionModel.getDbUrl())
                .append(":")
                .append(this.databaseConnectionModel.getDbPort())
                .append("/")
                .append(this.databaseConnectionModel.getDbName());
        return dbURL.toString();
    }
    public String createInsertSQL(){
        final String into = columns.stream()
                .map(String::toLowerCase)
                .collect(Collectors.joining(", "));
        final String values = columns.stream()
                .map(column -> ":" + column.toLowerCase())
                .collect(Collectors.joining(", "));
        stringBuilderClear();
        SQL
                .append("INSERT INTO ")
                .append(this.getTableSchema())
                .append(".")
                .append(this.getTableName())
                .append(" (")
                .append(into)
                .append(") VALUES (")
                .append(values)
                .append(")");
        return SQL.toString();
    }
    public DataSource getDataSource() {
        return this.dataSource;
    }
    public String getTableName() {
        return tableName;
    }
    public String getTableSchema() {
        return tableSchema;
    }
    public List<String> getColumns() {
        return columns;
    }
    public List<String> getColumnsDataTypes() {
        return columnsDataTypes;
    }

    public DatabaseConnectionModel getDatabaseConnectionModel() {
        return this.databaseConnectionModel;
    }


    private void stringBuilderClear(){
        this.SQL.delete(0,SQL.length());
    }
}
