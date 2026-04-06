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

    @Override
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    @Override
    public Voucher createVoucher(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public void deleteVoucher(Integer id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public Optional<Voucher> getByCode(String code) {
        return voucherRepository.findByCode(code);
    }
}