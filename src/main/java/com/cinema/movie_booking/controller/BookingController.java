package com.cinema.movie_booking.controller;

import com.cinema.movie_booking.dto.BookingRequest;
import com.cinema.movie_booking.dto.BookingResponse;
import com.cinema.movie_booking.dto.PriceCalculateRequest;
import com.cinema.movie_booking.dto.PriceCalculateResponse;
import com.cinema.movie_booking.service.impl.BookingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;

    // API 3: POST /api/bookings/calculate-price
    // Tính giá nháp (kèm voucher) KHÔNG lưu DB – dùng để hiển thị tóm tắt trước khi thanh toán
    @PostMapping("/calculate-price")
    public ResponseEntity<?> calculatePrice(@RequestBody PriceCalculateRequest request) {
        try {
            PriceCalculateResponse response = bookingService.calculatePrice(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API 4: POST /api/bookings
    // Chốt đơn: lưu DB, khóa ghế, trừ voucher, chặn 2 người đặt cùng ghế
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            BookingResponse response = bookingService.createBooking(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API Lấy lịch sử đặt vé của User: GET /api/bookings/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBookingHistory(@PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(bookingService.getUserBookings(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi lấy lịch sử đặt vé: " + e.getMessage());
        }
    }
}
