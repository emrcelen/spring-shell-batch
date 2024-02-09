package com.wenthor.batchJShell.enumeration;

public enum Database {
    POSTGRE("postgresql","org.postgresql.Driver");

    private String name;
    private String driver;

    Database(String name, String driver) {
        this.name = name;
        this.driver = driver;
    }

    public String getName() {
        return name;
    }
    public String getDriver() {
        return driver;
    }
}
