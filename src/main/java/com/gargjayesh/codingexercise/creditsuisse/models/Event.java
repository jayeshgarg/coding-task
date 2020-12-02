package com.gargjayesh.codingexercise.creditsuisse.models;

import lombok.Data;

@Data
public class Event {
    private String id;
    private State state;
    private long timestamp;
    private String type;//taking as string as not sure of other possible values
    private String host;

    public Event(final String id, final State state, final long timestamp, final String type, final String host) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        this.type = type;
        this.host = host;
    }
}
