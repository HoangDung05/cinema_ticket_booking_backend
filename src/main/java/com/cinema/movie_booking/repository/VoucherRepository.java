package com.cinema.movie_booking.repository;

import com.cinema.movie_booking.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {

    // Tìm voucher theo code (không phân biệt chữ hoa/thường)
    Optional<Voucher> findByCodeIgnoreCase(String code);

    // Tìm voucher hợp lệ: ACTIVE + còn thời hạn + chưa hết số lần dùng
    Optional<Voucher> findByCodeIgnoreCaseAndStatusAndStartDateBeforeAndEndDateAfter(
            String code,
            String status,
            LocalDateTime now1,
            LocalDateTime now2
    );
}
