package com.cinema.movie_booking.controller;

import com.cinema.movie_booking.dto.BookingRequest;
import com.cinema.movie_booking.dto.BookingResponse;
import com.cinema.movie_booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings") //
@RequiredArgsConstructor

public class BookingController {

    private final BookingService bookingService;

    /**
     * API Đặt vé mới
     * POST http://localhost:8080/api/bookings
     */
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            // Service xử lý logic: Check ghế, Áp voucher, Tính tiền, Lưu DB
            BookingResponse response = bookingService.createBooking(request);

            // Trả về 201 Created khi đặt thành công
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (RuntimeException e) {
            // Trả về lỗi nghiệp vụ (Ghế đã đặt, Voucher lỗi...)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Trả về lỗi hệ thống
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
    }

    /**
     * API Lấy lịch sử đặt vé
     * GET http://localhost:8080/api/bookings/user/1
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBookingHistory(@PathVariable Integer userId) {
        try {
            List<BookingResponse> history = bookingService.getUserBookings(userId);
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể lấy lịch sử đặt vé: " + e.getMessage());
        }
    }
}