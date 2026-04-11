package com.cinema.movie_booking.controller;

import com.cinema.movie_booking.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    // GET /api/vouchers : Lấy danh sách các mã đang ACTIVE
    @GetMapping
    public ResponseEntity<?> getActiveVouchers() {
        return ResponseEntity.ok(voucherService.getActiveVouchers());
    }

    // POST /api/vouchers/claim : Khách hàng ấn thu thập vé để lưu vào ví
    @PostMapping("/claim")
    public ResponseEntity<?> claimVoucher(@RequestBody Map<String, String> payload) {
        try {
            String email = payload.get("email");
            String code = payload.get("code");
            
            if (email == null || code == null) {
                return ResponseEntity.badRequest().body("Thiều thông tin email hoặc code");
            }
            
            return ResponseEntity.ok(voucherService.claimVoucher(email, code));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
