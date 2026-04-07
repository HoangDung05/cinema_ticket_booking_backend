package com.cinema.movie_booking.controller;

import com.cinema.movie_booking.entity.Showtime;
import com.cinema.movie_booking.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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