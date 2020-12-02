package com.gargjayesh.codingexercise.creditsuisse.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EventCounter extends Thread {
    private static final Logger logger = LogManager.getLogger(EventCounter.class);
    private final EventService eventService;

    EventCounter(final EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void run() {
        try {
            while (eventService.getAllEvents().size() < 10000000) {
                Thread.sleep(10 * 1000);//sleep for 10 secs before getting records count from db
                logger.warn("All recorded events are : {}", eventService.getAllEvents().size());
            }
        } catch (final Exception e) {
        }
    }
}