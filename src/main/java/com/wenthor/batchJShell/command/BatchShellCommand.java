package com.wenthor.batchJShell.command;

import com.wenthor.batchJShell.service.BatchService;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;

@Command(
        command = "batch",
        group = "Batch Command",
        description = "You define the necessary information to start the batch process with these commands.")
public class BatchShellCommand {
    private final BatchService batchService;

    public BatchShellCommand(BatchService batchService) {
        this.batchService = batchService;
    }

    @Command(command = "model", description = "You should use this command to create the batch model.")
    String databaseConnectionParameters(
            @Option(shortNames = 'f', longNames = "fileName", defaultValue = "localhost", description = "You need to enter the csv file name into this field.", required = true) String fileName,
            @Option(shortNames = 'm', longNames = "mail", defaultValue = "noreply@batchJShell.com", description = "You need to enter the mail into this field.", required = true) String mail,
            @Option(shortNames = 'd', longNames = "delimiter", defaultValue = ";", description = "You need to enter the csv delimiter into this field.") String delimiter){
        return this.batchService.selectFileAndDelimiter(delimiter,fileName,mail);
    }

    @Command(command = "start")
    void batchStart(){
        this.batchService.batchStarter();
    }
}
