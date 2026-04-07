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
    public Showtime updateShowtime(Integer id, Showtime details) {
        // 1. Tìm suất chiếu cũ trong DB
        Showtime existing = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu!"));

        // 2. Chỉ cập nhật những trường nào có gửi lên (Tránh null)
        if (details.getStartTime() != null)
            existing.setStartTime(details.getStartTime());
        if (details.getEndTime() != null)
            existing.setEndTime(details.getEndTime());
        if (details.getPrice() != null)
            existing.setPrice(details.getPrice());

        // Nếu có đổi phòng thì mới set phòng mới
        if (details.getRoom() != null && details.getRoom().getId() != null) {
            existing.setRoom(details.getRoom());
        }

        // 3. Lưu lại đối tượng đã cập nhật
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

    @Override
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    @Override
    public List<Showtime> getShowtimesByMovieId(Integer movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }
}