package com.cinema.movie_booking.controller;

import com.cinema.movie_booking.dto.SeatStatusDTO;
import com.cinema.movie_booking.dto.ShowtimeDTO;
import com.cinema.movie_booking.entity.Showtime;
import com.cinema.movie_booking.service.ShowtimeService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    // Lấy tất cả suất chiếu
    @GetMapping
    public ResponseEntity<List<Showtime>> getAll() {
        return ResponseEntity.ok(showtimeService.getAllShowtimes());
    }

    // Lấy 1 xuất chiếu
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(showtimeService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Lấy sơ đồ ghế của 1 suất chiếu: GET /api/showtimes/{id}/seats
    @GetMapping("/{id}/seats")
    public ResponseEntity<?> getSeatsByShowtime(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(showtimeService.getSeatsByShowtimeId(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}