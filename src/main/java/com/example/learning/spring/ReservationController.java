package com.example.learning.spring;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable("id") Long id) {
        try {
            log.info("Called get Reservation By Id={}", id);
            return ResponseEntity.ok(reservationService.getReservationById(id));
        } catch (NoSuchElementException e) {
            log.warn("Reservation not found: id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        log.info("Called get all reservations!");
        return ResponseEntity.ok(reservationService.findAllReservations());
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservationToCreate) {
        try {
            log.info("Called createReservation: {}", reservationToCreate);
            Reservation created = reservationService.createReservation(reservationToCreate);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            log.error("Invalid reservation data: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable("id") Long id,
                                                         @RequestBody Reservation reservationToUpdate) {
        try {
            log.info("Called updateReservation: id={}, data={}", id, reservationToUpdate);
            Reservation updated = reservationService.updateReservation(id, reservationToUpdate);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            log.warn("Reservation not found for update: id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            log.error("Cannot update reservation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable("id") Long id) {
        try {
            log.info("Called delete reservation: id={}", id);
            reservationService.deleteReservation(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            log.warn("Reservation not found for deletion: id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservation(@PathVariable("id") Long id) {
        try {
            log.info("Called approve reservation: id={}", id);
            Reservation approved = reservationService.approveReservation(id);
            return ResponseEntity.ok(approved);
        } catch (NoSuchElementException e) {
            log.warn("Reservation not found for approval: id={}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            log.error("Cannot approve reservation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable("userId") Long userId) {
        log.info("Called get reservations by userId={}", userId);
        return ResponseEntity.ok(reservationService.findByUserId(userId));
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Reservation>> getReservationsByRoom(@PathVariable("roomId") Long roomId) {
        log.info("Called get reservations by roomId={}", roomId);
        return ResponseEntity.ok(reservationService.findByRoomId(roomId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable("status") ReservationStatus status) {
        log.info("Called get reservations by status={}", status);
        return ResponseEntity.ok(reservationService.findByStatus(status));
    }
}