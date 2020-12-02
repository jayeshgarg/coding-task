package com.gargjayesh.codingexercise.creditsuisse.service;

import java.util.Map;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gargjayesh.codingexercise.creditsuisse.models.Event;
import com.gargjayesh.codingexercise.creditsuisse.util.ApplicationUtils;

public class EventConsumer implements Runnable {
    private final EventService eventService;
    private final Queue<String> eventQueue;
    private static final Logger logger = LogManager.getLogger(EventConsumer.class);
    private final Map<String, Event> orphanEvents;

    EventConsumer(final EventService eventService, final Queue<String> eventQueue, final Map<String, Event> orphanEvents) {
        this.eventService = eventService;
        this.eventQueue = eventQueue;
        this.orphanEvents = orphanEvents;
    }

    @Override
    public void run() {
        logger.info("Starting a new consumer thread.");
        int terminator = 0;
        while (true) {
            final String s = eventQueue.poll();
            if (s == null) {
                logger.debug("[Retry #{}] Queue was empty. Waiting for 250ms before retry.", terminator + 1);
                if (terminator == 4) {
                    break;
                }
                terminator++;
                ApplicationUtils.sleep(250);
            } else {
                logger.trace("Sending new log event for parsing.");
                terminator = 0;
                eventService.parseLogEvent(orphanEvents, s);
            }
        }
        //debug purpose only
        logger.warn("Total events in DB are : {}", eventService.getAllEvents().size());
        logger.info("Consumer thread job complete. Shutting down THIS thread.");
    }
}