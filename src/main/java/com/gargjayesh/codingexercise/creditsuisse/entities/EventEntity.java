package com.gargjayesh.codingexercise.creditsuisse.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "event")
@Data
public class EventEntity {

    @Id
    @Column(name = "event_id")
    private String id;

    @Column(name = "event_duration")
    private long duration;

    @Column(name = "event_type")
    private String type;

    @Column(name = "event_host")
    private String host;

    @Column(name = "event_alert")
    private Boolean alert;

    public EventEntity() {
    }

    public EventEntity(final String id, final long duration, final String type, final String host, final boolean alert) {
        this.id = id;
        this.duration = duration;
        this.type = type;
        this.host = host;
        this.alert = alert;
    }
}
