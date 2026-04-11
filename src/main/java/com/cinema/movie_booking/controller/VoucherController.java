package com.cinema.movie_booking.controller;

import com.cinema.movie_booking.entity.Voucher;
import com.cinema.movie_booking.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vouchers")
@RequiredArgsConstructor
public class VoucherController {

    private final VoucherService voucherService;

    /** Admin: toàn bộ voucher trong DB */
    @GetMapping("/all")
    public ResponseEntity<List<Voucher>> getAllVouchers() {
        return ResponseEntity.ok(voucherService.getAllVouchers());
    }

    // GET /api/vouchers : Lấy danh sách các mã đang ACTIVE (khách / checkout)
    @GetMapping
    public ResponseEntity<?> getActiveVouchers() {
        return ResponseEntity.ok(voucherService.getActiveVouchers());
    }

    @PostMapping
    public ResponseEntity<Voucher> createVoucher(@RequestBody Voucher voucher) {
        voucher.setId(null);
        if (voucher.getUsedCount() == null) {
            voucher.setUsedCount(0);
        }
        if (voucher.getStatus() == null || voucher.getStatus().isBlank()) {
            voucher.setStatus("ACTIVE");
        }
        Voucher saved = voucherService.createVoucher(voucher);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable Integer id, @RequestBody Voucher voucher) {
        return ResponseEntity.ok(voucherService.updateVoucher(id, voucher));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Integer id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
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
