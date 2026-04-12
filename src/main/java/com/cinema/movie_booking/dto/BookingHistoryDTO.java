package com.cinema.movie_booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingHistoryDTO {
    private Integer bookingId;
    private String movieTitle;
    /** Thời lượng phim (phút) — hiển thị khoảng giờ chiếu trên vé */
    private Integer movieDuration;
    private String posterUrl;
    private String cinemaName;
    private LocalDateTime showtimeStart;
    private BigDecimal totalPrice;
    private String status;
    private String seatNames; // VD: "A1, A2"
    /** Thời điểm tạo đơn — dùng cho đếm ngược thanh toán khi PENDING */
    private LocalDateTime bookingCreatedAt;
}
