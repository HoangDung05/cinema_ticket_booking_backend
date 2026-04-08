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

    // API 1: GET /api/movies/{movieId}/showtimes
    // Trả về lịch chiếu của phim kèm thông tin rạp, phòng (dùng cho trang Chi tiết Phim)
    @GetMapping("/movies/{movieId}/showtimes")
    public ResponseEntity<?> getShowtimesByMovie(@PathVariable Integer movieId) {
        try {
            List<ShowtimeDTO> showtimes = showtimeService.getShowtimeDTOsByMovieId(movieId);
            return ResponseEntity.ok(showtimes);
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