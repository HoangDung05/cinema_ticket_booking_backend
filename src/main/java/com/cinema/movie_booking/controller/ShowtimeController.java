package com.cinema.movie_booking.controller;

import com.cinema.movie_booking.dto.SeatStatusDTO;
import com.cinema.movie_booking.dto.ShowtimeDTO;
import com.cinema.movie_booking.entity.Showtime;
import com.cinema.movie_booking.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    /**
     * 2. Lấy chi tiết một suất chiếu
     * GET http://localhost:8080/api/showtimes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(showtimeService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 3. Thêm suất chiếu mới (Dành cho Admin)
     * POST http://localhost:8080/api/showtimes
     * Body JSON: { "movie": {"id": 1}, "room": {"id": 1}, "startTime":
     * "2026-05-01T19:00:00", "endTime": "2026-05-01T21:00:00", "price": 95000 }
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Showtime showtime) {
        try {
            Showtime created = showtimeService.createShowtime(showtime);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Trả về lỗi nếu trùng lịch chiếu
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * 4. Cập nhật suất chiếu
     * PUT http://localhost:8080/api/showtimes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Showtime showtime) {
        try {
            return ResponseEntity.ok(showtimeService.updateShowtime(id, showtime));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 5. Xóa suất chiếu
     * DELETE http://localhost:8080/api/showtimes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            showtimeService.deleteShowtime(id);
            return ResponseEntity.ok("Xóa thành công suất chiếu ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Không thể xóa: " + e.getMessage());
        }
    }
}