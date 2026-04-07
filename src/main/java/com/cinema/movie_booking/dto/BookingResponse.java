package com.cinema.movie_booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BookingResponse {
    private Integer bookingId;
    private BigDecimal totalPrice;
    private BigDecimal discountAmount;
    private String status;
}