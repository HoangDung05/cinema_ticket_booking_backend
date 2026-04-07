package com.cinema.movie_booking.service;

import com.cinema.movie_booking.dto.SeatStatusDTO;
import com.cinema.movie_booking.dto.ShowtimeDTO;
import com.cinema.movie_booking.entity.Showtime;

import java.util.List;

public interface ShowtimeService {
    // Legacy method (kept for backward compat)
    List<Showtime> getShowtimesByMovieId(Integer movieId);

    // API 1: GET /api/movies/{movieId}/showtimes
    // Trả về lịch chiếu của phim kèm thông tin rạp/phòng
    List<ShowtimeDTO> getShowtimeDTOsByMovieId(Integer movieId);

    // API 2: GET /api/showtimes/{id}/seats
    // Trả về sơ đồ ghế của suất chiếu: gồm trạng thái đã đặt hay còn trống
    List<SeatStatusDTO> getSeatsByShowtimeId(Integer showtimeId);
}
