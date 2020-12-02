package com.gargjayesh.codingexercise.creditsuisse.service;

import static com.gargjayesh.codingexercise.creditsuisse.models.State.FINISHED;
import static com.gargjayesh.codingexercise.creditsuisse.models.State.STARTED;
import static com.gargjayesh.codingexercise.creditsuisse.util.ErrorCode.ILLEGAL_EVENT;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gargjayesh.codingexercise.creditsuisse.entities.EventEntity;
import com.gargjayesh.codingexercise.creditsuisse.exception.ApplicationException;
import com.gargjayesh.codingexercise.creditsuisse.models.Event;
import com.gargjayesh.codingexercise.creditsuisse.repositories.EventRepository;
import com.google.gson.Gson;

@Service
class EventService {
    private static final Logger logger = LogManager.getLogger(EventService.class);

    private final EventRepository eventRepository;

    private final Gson gson;

    private Map<String, Event> orphanEvents;

    @Autowired
    public EventService(final EventRepository eventRepository, final Gson gson) {
        this.eventRepository = eventRepository;
        this.gson = gson;
    }

    void parseLogEvent(final Map<String, Event> orphanEvents, final String logEventStr) {
        this.orphanEvents = orphanEvents;
        logger.debug("Parsing log event");
        final Event event = gson.fromJson(logEventStr, Event.class);
        logger.debug("Log event parsed successfully.");
        logger.debug("Sending event for further processing");
        processIndividualEvent(event);
    }

    private void processIndividualEvent(final Event event) {
        if (event.getState() == STARTED || event.getState() == FINISHED) {
            final Event orphan = orphanEvents.get(event.getId());
            logger.debug("Orphan from map = {}", orphan);
            if (orphan == null) {
                logger.debug("{} - adding event to orphan group", event.getId());
                orphanEvents.put(event.getId(), event);
            } else {
                logger.debug("{} - event pair found", event.getId());
                orphanEvents.remove(event.getId());
                logger.debug("Event timestamp = {}, Orphan event timestamp = {}", event.getTimestamp(), orphan.getTimestamp());
                final long start = event.getState().equals(STARTED) ? event.getTimestamp() : orphan.getTimestamp();
                final long end = event.getState().equals(FINISHED) ? event.getTimestamp() : orphan.getTimestamp();
                createEventEntityInDB(event.getId(), start, end, event.getType(), event.getHost());
            }
        } else {
            logger.error("Invalid event encountered. Log event invalid. Skipping log event.",
                    new ApplicationException(ILLEGAL_EVENT, "Invalid event encountered. Log event invalid. Skipping log event."));
        }

        logger.debug("Processed event = {}", event);
    }

    private void createEventEntityInDB(final String id, final long start, final long end,
            final String type, final String host) {
        final long duration = end - start;
        final boolean alert = duration > 4;
        final EventEntity eventForDb = new EventEntity(id, duration, type, host, alert);
        logger.debug("Saving event in DB");
        eventRepository.save(eventForDb);
    }

    List<EventEntity> getAllEvents() {
        return eventRepository.findAll();
    }
}
