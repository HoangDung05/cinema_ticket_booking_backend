package com.cinema.movie_booking.service;

import com.cinema.movie_booking.entity.Showtime;
import java.util.List;

public interface ShowtimeService {
    // 1. Cho Khách hàng: Lấy suất chiếu theo phim (chưa diễn ra)
    List<Showtime> getByMovieId(Integer movieId);

    // 2. Cho Admin: Thêm mới suất chiếu (có check trùng lịch)
    Showtime createShowtime(Showtime showtime);

    // 3. Cho Admin: Cập nhật suất chiếu
    Showtime updateShowtime(Integer id, Showtime showtime);

    // 4. Xóa suất chiếu
    void deleteShowtime(Integer id);

    // 5. Lấy chi tiết 1 suất chiếu
    Showtime getById(Integer id);

    List<Showtime> getAllShowtimes();

    List<Showtime> getShowtimesByMovieId(Integer movieId);
}