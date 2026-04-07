package com.cinema.movie_booking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cinema.movie_booking.entity.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    // Tìm voucher theo mã code (Ví dụ: "GIAM50K")
    Optional<Voucher> findByCode(String code);

    // Tìm các voucher còn hạn sử dụng và trạng thái ACTIVE
    List<Voucher> findByStatusAndEndDateAfter(String status, java.time.LocalDateTime now);
}