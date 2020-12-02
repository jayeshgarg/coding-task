package com.gargjayesh.codingexercise.creditsuisse.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gargjayesh.codingexercise.creditsuisse.util.ApplicationUtils;

public class DbCounter implements Runnable {
    private final EventService eventService;
    private static final Logger logger = LogManager.getLogger(EventConsumer.class);

    DbCounter(final EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void run() {
        logger.info("Starting a new db record counter thread.");
        int lastCount = -1;
        int currentCount = 0;
        while (lastCount != currentCount) {//terminate this monitor if there is no change in last 3 secs
            ApplicationUtils.sleep(3000);
            logger.debug("Total events in DB are {}", eventService.getAllEvents().size());
            lastCount = currentCount;
            currentCount = eventService.getAllEvents().size();
        }
        logger.info("DB records are all processed. Shutting down monitor thread.");
    }
}
