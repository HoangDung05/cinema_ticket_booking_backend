package com.cinema.movie_booking.controller;

import com.cinema.movie_booking.entity.Showtime;
import com.cinema.movie_booking.service.ShowtimeService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/showtimes")
@RequiredArgsConstructor

public class ShowtimeController {

    private final ShowtimeService showtimeService;

    /**
     * 1. Lấy danh sách suất chiếu của một phim (Dành cho Khách hàng)
     * GET http://localhost:8080/api/showtimes/movie/{movieId}
     */
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getByMovie(@PathVariable Integer movieId) {
        try {
            List<Showtime> list = showtimeService.getByMovieId(movieId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // Tất cả suất chiếu
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

}