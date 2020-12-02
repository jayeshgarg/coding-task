package com.gargjayesh.codingexercise.creditsuisse;

import static com.gargjayesh.codingexercise.creditsuisse.util.ErrorCode.INCORRECT_NO_OF_ARGS;
import static com.gargjayesh.codingexercise.creditsuisse.util.ErrorCode.INVALID_FILE_PATH;
import static java.lang.System.exit;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import com.gargjayesh.codingexercise.creditsuisse.exception.ApplicationException;
import com.gargjayesh.codingexercise.creditsuisse.service.EventProcessingManager;

@SpringBootApplication
@Configuration
public class Main implements CommandLineRunner {
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Autowired
    private EventProcessingManager eventProcessingManager;

    public static void main(final String[] args) {
        logger.info("STARTING THE APPLICATION");
        SpringApplication.run(Main.class, args);
        logger.info("APPLICATION FINISHED");

    }

    @Override
    public void run(final String... args) {
        logger.info("Processing log file...");
        if (args.length != 1 || args[0].length() < 1) {
            logger.error("Invalid number of arguments provided. Exactly 1 argument is expected",
                    new ApplicationException(INCORRECT_NO_OF_ARGS, "Invalid number of arguments provided. Exactly 1 argument is expected."));
            exit(0);
        }
        try {
            final File logFile = new File(args[0]);
            logFile.getCanonicalPath();
            if (logFile.exists()) {
                logger.debug("Calling event processing manager to process the log file");
                eventProcessingManager.startEventProcessingManager(args[0]);
            } else {
                logger.error("Either file path is invalid or it doesn't exist. Please check log file path.",
                        new ApplicationException(INVALID_FILE_PATH, "Either file path is invalid or it doesn't exist. Please check log file path."));
                exit(0);
            }
        } catch (final IOException e) {
            logger.error("Either file path is invalid or it doesn't exist. Please check log file path.",
                    new ApplicationException(INVALID_FILE_PATH, "Either file path is invalid or it doesn't exist. Please check log file path."));
            exit(0);
        }
        logger.info("Processing complete.");
    }
}
