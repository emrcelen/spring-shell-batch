package com.wenthor.batchJShell.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

import java.util.Map;

public class ReadListener implements ItemReadListener<Map<String, Object>> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void beforeRead() {
        log.debug("Reading a new record.");
    }

    @Override
    public void afterRead(Map<String, Object> item) {
        log.debug("New record read: {}", item);
    }

    @Override
    public void onReadError(Exception ex) {
        log.error("Error in reading the record : " + ex.getMessage());
    }
}
