package com.cinema.movie_booking.service.impl;

import com.cinema.movie_booking.dto.BookingRequest;
import com.cinema.movie_booking.dto.BookingResponse;
import com.cinema.movie_booking.entity.*;
import com.cinema.movie_booking.repository.*;
import com.cinema.movie_booking.service.BookingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final ShowtimeRepository showtimeRepository;
    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // 1. Tìm thông tin cơ bản
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new RuntimeException("Suất chiếu không tồn tại"));

        // 2. Check trùng ghế ngay lập tức
        for (Integer seatId : request.getSeatIds()) {
            if (bookingDetailRepository.existsByShowtimeIdAndSeatId(request.getShowtimeId(), seatId)) {
                throw new RuntimeException("Ghế ID " + seatId + " đã bị đặt trước đó!");
            }
        }

        // 3. Tính toán tiền bạc
        BigDecimal basePrice = showtime.getPrice().multiply(new BigDecimal(request.getSeatIds().size()));
        BigDecimal discountAmount = BigDecimal.ZERO;
        Voucher voucher = null;

        // 4. Logic Voucher
        if (request.getVoucherCode() != null && !request.getVoucherCode().isEmpty()) {
            voucher = voucherRepository.findByCode(request.getVoucherCode())
                    .orElseThrow(() -> new RuntimeException("Mã voucher không hợp lệ"));

            // Check hạn dùng (giả định)
            if (LocalDateTime.now().isAfter(voucher.getEndDate())) {
                throw new RuntimeException("Voucher đã hết hạn");
            }

            discountAmount = calculateDiscount(voucher, basePrice);
            voucher.setUsedCount(voucher.getUsedCount() + 1);
            voucherRepository.save(voucher);
        }

        // 5. Lưu Booking chính
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShowtime(showtime);
        booking.setTotalPrice(basePrice.subtract(discountAmount));
        booking.setDiscountAmount(discountAmount);
        booking.setVoucher(voucher);
        booking.setStatus("PENDING");

        Booking savedBooking = bookingRepository.save(booking);

        // 6. Lưu BookingDetail (Danh sách ghế)
        for (Integer seatId : request.getSeatIds()) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy ghế"));

            BookingDetail detail = new BookingDetail();
            detail.setBooking(savedBooking);
            detail.setShowtime(showtime);
            detail.setSeat(seat);
            detail.setPriceAtBooking(showtime.getPrice());
            bookingDetailRepository.save(detail);
        }

        return new BookingResponse(savedBooking.getId(), savedBooking.getTotalPrice(),
                savedBooking.getDiscountAmount(), savedBooking.getStatus());
    }

    @Override
    public List<BookingResponse> getUserBookings(Integer userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(b -> new BookingResponse(b.getId(), b.getTotalPrice(), b.getDiscountAmount(), b.getStatus()))
                .collect(Collectors.toList());
    }

    private BigDecimal calculateDiscount(Voucher v, BigDecimal amount) {
        if ("PERCENT".equals(v.getDiscountType())) {
            return amount.multiply(v.getDiscountValue()).divide(new BigDecimal(100));
        }
        return v.getDiscountValue();
    }

    @Override
    public Booking createBooking(Integer userId, Integer showtimeId, List<Integer> seatIds, String voucherCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBooking'");
    }

    @Override
    public List<Booking> getHistoryByUserId(Integer userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHistoryByUserId'");
    }

    @Override
    public Booking getBookingById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBookingById'");
    }
}