package com.gargjayesh.codingexercise.creditsuisse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gargjayesh.codingexercise.creditsuisse.entities.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, String> {
}
