package com.cinema.movie_booking.service;

import java.util.List;

import com.cinema.movie_booking.dto.AdminStatsDTO;
import com.cinema.movie_booking.dto.BookingRequest;
import com.cinema.movie_booking.dto.BookingResponse;
import com.cinema.movie_booking.entity.Booking;

public interface BookingService {
    Booking createBooking(Integer userId, Integer showtimeId, List<Integer> seatIds, String voucherCode);

    List<Booking> getHistoryByUserId(Integer userId);

    Booking getBookingById(Integer id);

    BookingResponse createBooking(BookingRequest request);

    List<BookingResponse> getUserBookings(Integer userId);

    List<Booking> getAllBookingsForAdmin();

    AdminStatsDTO getAdminStats();
}