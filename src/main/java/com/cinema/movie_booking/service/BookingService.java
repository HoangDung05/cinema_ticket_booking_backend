package com.cinema.movie_booking.service;

import java.util.List;

import com.cinema.movie_booking.dto.AdminStatsDTO;
import com.cinema.movie_booking.entity.Booking;

public interface BookingService {
    List<Booking> getAllBookingsForAdmin();

    AdminStatsDTO getAdminStats();
}