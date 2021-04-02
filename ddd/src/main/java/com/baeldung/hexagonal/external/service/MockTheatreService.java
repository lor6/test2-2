package com.baeldung.hexagonal.external.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class MockTheatreService implements TheatreService {

    public ResponseEntity<Reservation> postReservation(String theatreId, String movieShowId, Set<String> seats) {
        return new ResponseEntity<Reservation>(
                new Reservation(UUID.randomUUID().toString()), HttpStatus.CREATED);
    }

    public ResponseEntity<?> deleteReservation(String reservationId) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
