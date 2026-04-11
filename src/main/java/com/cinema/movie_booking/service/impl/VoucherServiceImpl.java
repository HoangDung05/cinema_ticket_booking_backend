package com.cinema.movie_booking.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cinema.movie_booking.entity.Voucher;
import com.cinema.movie_booking.repository.VoucherRepository;
import com.cinema.movie_booking.service.VoucherService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final com.cinema.movie_booking.repository.UserVoucherRepository userVoucherRepository;
    private final com.cinema.movie_booking.service.UserService userService;

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher createVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher updateVoucher(Integer id, Voucher incoming) {
        Voucher existing = voucherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher id: " + id));
        existing.setCode(incoming.getCode());
        existing.setDescription(incoming.getDescription());
        existing.setDiscountType(incoming.getDiscountType());
        existing.setDiscountValue(incoming.getDiscountValue());
        existing.setMinOrderValue(incoming.getMinOrderValue());
        existing.setMaxDiscountAmount(incoming.getMaxDiscountAmount());
        existing.setStartDate(incoming.getStartDate());
        existing.setEndDate(incoming.getEndDate());
        existing.setUsageLimit(incoming.getUsageLimit());
        existing.setUsedCount(incoming.getUsedCount() != null ? incoming.getUsedCount() : existing.getUsedCount());
        existing.setStatus(incoming.getStatus());
        return voucherRepository.save(existing);
    }

    @Override
    public void deleteVoucher(Integer id) {
        if (!voucherRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy voucher id: " + id);
        }
        voucherRepository.deleteById(id);
    }

    @Override
    public Optional<Voucher> getByCode(String code) {
        return voucherRepository.findByCodeIgnoreCase(code);
    }

    @Override
    public List<Voucher> getActiveVouchers() {
        return voucherRepository.findActiveVouchers();
    }

    @Override
    public List<com.cinema.movie_booking.entity.UserVoucher> getUserVouchers(String email) {
        com.cinema.movie_booking.entity.User user = userService.getByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return userVoucherRepository.findByUser(user);
    }

    @Override
    public com.cinema.movie_booking.entity.UserVoucher claimVoucher(String email, String code) {
        com.cinema.movie_booking.entity.User user = userService.getByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Voucher voucher = voucherRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new RuntimeException("Voucher code not found: " + code));

        if (!"ACTIVE".equals(voucher.getStatus())) {
            throw new RuntimeException("Voucher is not active");
        }
        if (voucher.getUsageLimit() != null && voucher.getUsedCount() >= voucher.getUsageLimit()) {
            throw new RuntimeException("Voucher usage limit reached");
        }
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        if (now.isBefore(voucher.getStartDate()) || now.isAfter(voucher.getEndDate())) {
            throw new RuntimeException("Voucher is not valid at this time");
        }

        Optional<com.cinema.movie_booking.entity.UserVoucher> existing = userVoucherRepository.findByUserAndVoucher(user, voucher);
        if (existing.isPresent()) {
            throw new RuntimeException("You have already claimed this voucher");
        }

        com.cinema.movie_booking.entity.UserVoucher userVoucher = new com.cinema.movie_booking.entity.UserVoucher();
        userVoucher.setUser(user);
        userVoucher.setVoucher(voucher);
        // assignedAt is handled by @PrePersist in UserVoucher entity
        
        return userVoucherRepository.save(userVoucher);
    }
}