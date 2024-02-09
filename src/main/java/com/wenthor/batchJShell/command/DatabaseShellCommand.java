package com.wenthor.batchJShell.command;

import com.wenthor.batchJShell.enumeration.Database;
import com.wenthor.batchJShell.service.DatabaseService;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(
        command = "db",
        group = "Database Connection Command",
        description = "You can use the following commands to define the connection information for connecting to the database.")
public class DatabaseShellCommand {
    private final DatabaseService databaseService;

    public DatabaseShellCommand(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Command(command = "model", description = "The command used to enter the database information for insertion.")
    String databaseConnectionParameters(
            @Option(shortNames = 'u', longNames = "url", defaultValue = "localhost", description = "You need to enter the database url into this field.") String dbUrl,
            @Option(shortNames = 'p', longNames = "port", description = "You need to enter the database port into this field.", required = true) String dbPort,
            @Option(shortNames = 'n', longNames = "name", description = "You need to enter the database name into this field.", required = true) String dbName,
            @Option(longNames = {"un","username"}, description = "You need to enter the database username into this field.", required = true) String dbUserName,
            @Option(longNames = {"ps", "password"}, description = "You need to enter the database password into this field.", required = true) String dbPassword,
            @Option(longNames = "select", defaultValue = "postgre", description = "You need to enter the database into this field.") String db){
        this.databaseService.createDatabaseConnectionModel(
                dbUrl,
                dbPort,
                dbName,
                dbUserName,
                dbPassword,
                selectDatabase(db));
        return databaseService.getDatabaseConnectionModel().toString();
    }
    @Command(command = "source", description = "The command needed to establish the DB connection.")
    void createDataSource(){
        if(this.databaseService.getDatabaseConnectionModel().getDatabase().getName().equalsIgnoreCase("postgresql"))
            this.databaseService.createDataSource(this.databaseService.createPostgreUrl());
    }
    @Command(command = "table")
    String selectDatabaseTable(
            @Option(shortNames = 'n', longNames = "name", description = "You need to enter the table name into this field.", required = true) String tableName,
            @Option(shortNames = 's', longNames = "schema", description = "You need to enter the table schema into this field.", defaultValue = "public") String tableSchema){
        this.databaseService.getDatabaseColumnsName(tableName,tableSchema);
        return "Table Name: "
                . concat(this.databaseService.getTableName())
                .concat(" Schema: ")
                .concat(this.databaseService.getTableSchema())
                .concat(" Columns: ".concat(this.databaseService.getColumns().toString()))
                .concat("Data Types: ".concat(this.databaseService.getColumnsDataTypes().toString()));
    }
    private Database selectDatabase(String db){
        if(db.equalsIgnoreCase("postgre"))
            return Database.POSTGRE;
        throw new IllegalArgumentException("Please enter a valid database information. Example: Postgre.");
    }
}
