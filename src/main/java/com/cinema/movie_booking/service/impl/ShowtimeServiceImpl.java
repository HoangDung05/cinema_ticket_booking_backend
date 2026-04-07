package com.cinema.movie_booking.service.impl;

import com.cinema.movie_booking.entity.Showtime;
import com.cinema.movie_booking.repository.ShowtimeRepository;
import com.cinema.movie_booking.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;

    @Override
    public List<Showtime> getByMovieId(Integer movieId) {

        return showtimeRepository.findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(
                movieId, LocalDateTime.now());
    }

    @Override
    public Showtime createShowtime(Showtime showtime) {

        boolean isOverlapping = showtimeRepository.existsOverlappingShowtime(
                showtime.getRoom().getId(),
                showtime.getStartTime(),
                showtime.getEndTime(),
                null);

        if (isOverlapping) {
            throw new RuntimeException("Lỗi: Phòng này đã có lịch chiếu khác trong khoảng thời gian này!");
        }

        return showtimeRepository.save(showtime);
    }

    @Override
    public Showtime updateShowtime(Integer id, Showtime showtime) {
        Showtime existing = getById(id);

        // Check trùng lịch, loại trừ chính nó (id hiện tại)
        boolean isOverlapping = showtimeRepository.existsOverlappingShowtime(
                showtime.getRoom().getId(),
                showtime.getStartTime(),
                showtime.getEndTime(),
                id);

        if (isOverlapping) {
            throw new RuntimeException("Lỗi: Thời gian cập nhật bị trùng với lịch chiếu khác!");
        }

        existing.setStartTime(showtime.getStartTime());
        existing.setEndTime(showtime.getEndTime());
        existing.setPrice(showtime.getPrice());
        existing.setMovie(showtime.getMovie());
        existing.setRoom(showtime.getRoom());

        return showtimeRepository.save(existing);
    }

    @Override
    public Showtime getById(Integer id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu ID: " + id));
    }

    @Override
    public void deleteShowtime(Integer id) {
        showtimeRepository.deleteById(id);
    }
}