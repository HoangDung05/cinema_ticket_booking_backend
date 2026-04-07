package com.cinema.movie_booking.repository;

import com.cinema.movie_booking.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {
        List<Showtime> findByMovieId(Integer movieId);

        // 1. Tìm suất chiếu theo phim và thời gian bắt đầu sau thời điểm 'now'
        // Giúp khách hàng không thấy các suất đã chiếu xong
        List<Showtime> findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(Integer movieId, LocalDateTime now);

        // 2. Tìm tất cả suất chiếu trong một khoảng ngày nhất định (cho Admin quản lý)
        List<Showtime> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

        // 3. Logic kiểm tra trùng lịch chiếu (Nâng cao cho Admin)
        // Kiểm tra xem trong phòng 'roomId', có suất chiếu nào giao thoa với khoảng
        // [newStart, newEnd] không
        @Query("SELECT COUNT(s) > 0 FROM Showtime s WHERE s.room.id = :roomId " +
                        "AND (:newStart < s.endTime AND :newEnd > s.startTime) " +
                        "AND (:excludeId IS NULL OR s.id <> :excludeId)")
        boolean existsOverlappingShowtime(@Param("roomId") Integer roomId,
                        @Param("newStart") LocalDateTime newStart,
                        @Param("newEnd") LocalDateTime newEnd,
                        @Param("excludeId") Integer excludeId);
}