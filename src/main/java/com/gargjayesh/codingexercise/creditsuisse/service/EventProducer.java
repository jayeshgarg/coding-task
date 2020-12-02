package com.gargjayesh.codingexercise.creditsuisse.service;

import static com.gargjayesh.codingexercise.creditsuisse.util.ErrorCode.UNABLE_TO_OPEN_FILE;

import java.io.File;
import java.io.IOException;
import java.util.Queue;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gargjayesh.codingexercise.creditsuisse.exception.ApplicationException;

public class EventProducer implements Runnable {

    private final String logFilename;
    private final Queue<String> eventQueue;

    private static final Logger logger = LogManager.getLogger(EventProducer.class);

    EventProducer(final Queue<String> eventQueue, final String logFilename) {
        this.logFilename = logFilename;
        this.eventQueue = eventQueue;
    }

    @Override
    public void run() {
        logger.info("Starting a new producer thread.");
        try (final LineIterator itr = FileUtils.lineIterator(new File(logFilename), "UTF-8")) {
            while (itr.hasNext()) {
                eventQueue.add(itr.nextLine());
                logger.trace("New event added to queue");
            }
        } catch (final IOException e) {
            logger.error("Unable to open file for processing. Please check file.",
                    new ApplicationException(UNABLE_TO_OPEN_FILE, "Unable to open file for processing. Please check file."));
        }
        logger.info("Producer thread job complete. Shutting down THIS thread.");
    }
}
