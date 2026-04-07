package com.cinema.movie_booking.service;

import java.util.List;
import java.util.Optional;

import com.cinema.movie_booking.entity.Voucher;

public interface VoucherService {
    List<Voucher> getAllVouchers();

    Voucher createVoucher(Voucher voucher);

    void deleteVoucher(Integer id);

    Optional<Voucher> getByCode(String code);
}