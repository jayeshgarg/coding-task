package com.gargjayesh.codingexercise.creditsuisse.util;

import static com.gargjayesh.codingexercise.creditsuisse.util.ErrorCode.THREAD_SLEEP_INTERRUPTED;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.gargjayesh.codingexercise.creditsuisse.exception.ApplicationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class ApplicationUtils {
    private static final Logger logger = LogManager.getLogger(ApplicationUtils.class);

    @Bean
    public Gson getGson() {
        return new GsonBuilder().create();
    }

    public static void sleep(final int ms) {
        try {
            Thread.sleep(ms);
        } catch (final InterruptedException e) {
            logger.error("Thread sleep interrupted.",
                    new ApplicationException(THREAD_SLEEP_INTERRUPTED, "Thread sleep interrupted."));
        }
    }
}
