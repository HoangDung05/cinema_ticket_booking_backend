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

    // API 2: GET /api/showtimes/{id}/seats
    // Trả về sơ đồ ghế: ghế nào đã đặt, ghế nào còn trống (dùng cho trang Chọn Ghế)
    @GetMapping("/showtimes/{id}/seats")
    public ResponseEntity<?> getSeatsByShowtime(@PathVariable Integer id) {
        try {
            List<SeatStatusDTO> seats = showtimeService.getSeatsByShowtimeId(id);
            return ResponseEntity.ok(seats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // Legacy: GET /api/showtimes/movie/{movieId} (kept for backward compat)
    @GetMapping("/showtimes/movie/{movieId}")
    public ResponseEntity<List<Showtime>> getShowtimesByMovieLegacy(@PathVariable Integer movieId) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovieId(movieId));
    }
}
