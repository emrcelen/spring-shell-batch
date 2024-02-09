package com.wenthor.batchJShell.model;

import com.wenthor.batchJShell.enumeration.Database;

public class DatabaseConnectionModel {
    private String dbUrl;
    private String dbPort;
    private String dbName;
    private String dbUserName;
    private String dbPassword;
    private Database database;

    public DatabaseConnectionModel(Builder builder){
        this.dbUrl = builder.dbUrl;
        this.dbPort = builder.dbPort;
        this.dbName = builder.dbName;
        this.dbUserName = builder.dbUserName;
        this.dbPassword = builder.dbPassword;
        this.database = builder.database;
    }

    public String getDbUrl() {
        return dbUrl;
    }
    public String getDbPort() {
        return dbPort;
    }
    public String getDbName() {
        return dbName;
    }
    public String getDbUserName() {
        return dbUserName;
    }
    public String getDbPassword() {
        return dbPassword;
    }
    public Database getDatabase() {
        return database;
    }

    @Override
    public String toString() {
        return "DatabaseConnectionModel{" +
                "dbUrl='" + dbUrl + '\'' +
                ", dbPort='" + dbPort + '\'' +
                ", dbName='" + dbName + '\'' +
                ", dbUserName='" + dbUserName + '\'' +
                ", dbPassword='" + dbPassword + '\'' +
                ", database=" + database +
                '}';
    }

    public static class Builder{
        private String dbUrl;
        private String dbPort;
        private String dbName;
        private String dbUserName;
        private String dbPassword;
        private Database database;

        public Builder url(String dbUrl){
            this.dbUrl = dbUrl;
            return this;
        }
        public Builder port(String dbPort){
            this.dbPort = dbPort;
            return this;
        }
        public Builder name(String dbName){
            this.dbName = dbName;
            return this;
        }
        public Builder userName(String dbUserName){
            this.dbUserName = dbUserName;
            return this;
        }
        public Builder password(String dbPassword){
            this.dbPassword = dbPassword;
            return this;
        }
        public Builder database(Database database){
            this.database = database;
            return this;
        }
        public DatabaseConnectionModel build(){
            return new DatabaseConnectionModel(this);
        }
    }
}
