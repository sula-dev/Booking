package com.example.learning.spring;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found reservation by id = " + id));
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation createReservation(Reservation reservationToCreate) {
        if (reservationToCreate.getId() != null) {
            throw new IllegalArgumentException("Id should be empty!");
        }
        if (reservationToCreate.getStatus() != null) {
            throw new IllegalArgumentException("Status should be empty!");
        }

        reservationToCreate.setStatus(ReservationStatus.PENDING);
        return reservationRepository.save(reservationToCreate);
    }

    @Transactional
    public Reservation updateReservation(Long id, Reservation reservationToUpdate) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found reservation by id=" + id));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Cannot modify reservation: status=" + reservation.getStatus());
        }

        reservation.setUserId(reservationToUpdate.getUserId());
        reservation.setRoomId(reservationToUpdate.getRoomId());
        reservation.setStartDate(reservationToUpdate.getStartDate());
        reservation.setEndDate(reservationToUpdate.getEndDate());

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteReservation(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new NoSuchElementException("Not found reservation by id!");
        }
        reservationRepository.deleteById(id);
    }

    @Transactional
    public Reservation approveReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found reservation by id!"));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Cannot approve reservation: status=" + reservation.getStatus());
        }

        if (isReservationConflict(reservation)) {
            throw new IllegalArgumentException("Cannot approve reservation because of conflict!");
        }

        reservation.setStatus(ReservationStatus.APPROVED);
        return reservationRepository.save(reservation);
    }

    private boolean isReservationConflict(Reservation reservation) {
        List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                reservation.getRoomId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getId()
        );

        return !conflicts.isEmpty();
    }

    public List<Reservation> findByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> findByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }
}