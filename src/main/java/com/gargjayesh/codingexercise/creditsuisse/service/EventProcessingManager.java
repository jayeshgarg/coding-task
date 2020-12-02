package com.gargjayesh.codingexercise.creditsuisse.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gargjayesh.codingexercise.creditsuisse.models.Event;

@Service
public class EventProcessingManager {
    private static final Logger logger = LogManager.getLogger(EventProcessingManager.class);

    //assuming 1 producer thread
    private static final int PRODUCER_POOL_SIZE = 1;

    //assuming 2 consumer threads
    private static final int CONSUMER_POOL_SIZE = 2;

    private final Queue<String> eventQueue = new ArrayBlockingQueue<>(30000000);

    private static final Map<String, Event> orphanEvents = new HashMap<>();

    private final EventService eventService;

    @Autowired
    public EventProcessingManager(final EventService eventService) {
        this.eventService = eventService;
    }

    public void startEventProcessingManager(final String logFilename) {
        logger.debug("Starting a new producer thread pool executor for log file '{}' for processing.", logFilename);
        final ThreadPoolExecutor producerExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(PRODUCER_POOL_SIZE);
        producerExecutor.submit(new EventProducer(eventQueue, logFilename));//first reads the whole file into memory
        producerExecutor.submit(new DbCounter(eventService));//then starts a monitor thread

        logger.debug("Starting a new consumer thread pool executor for log file '{}' for processing.", logFilename);
        final ThreadPoolExecutor consumerExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(CONSUMER_POOL_SIZE);
        for (int i = 0; i < CONSUMER_POOL_SIZE; i++) {
            consumerExecutor.submit(new EventConsumer(eventService, eventQueue, orphanEvents));
        }

        producerExecutor.shutdown();
        consumerExecutor.shutdown();
    }
}
