package com.cinema.movie_booking.service.impl;

import com.cinema.movie_booking.dto.SeatStatusDTO;
import com.cinema.movie_booking.dto.ShowtimeDTO;
import com.cinema.movie_booking.entity.Seat;
import com.cinema.movie_booking.entity.Showtime;
import com.cinema.movie_booking.repository.BookingDetailRepository;
import com.cinema.movie_booking.repository.SeatRepository;
import com.cinema.movie_booking.repository.ShowtimeRepository;
import com.cinema.movie_booking.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final BookingDetailRepository bookingDetailRepository;

    // 1. Lấy suất chiếu theo phim (còn sắp chiếu) - legacy entity
    @Override
    public List<Showtime> getByMovieId(Integer movieId) {
        return showtimeRepository.findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(
                movieId, LocalDateTime.now());
    }

    // 2. Lấy suất chiếu kèm thông tin rạp/phòng (dùng cho trang Chi tiết Phim)
    @Override
    @Transactional(readOnly = true)
    public List<ShowtimeDTO> getShowtimeDTOsByMovieId(Integer movieId) {
        List<Showtime> showtimes = showtimeRepository
                .findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(movieId, LocalDateTime.now());

        return showtimes.stream().map(s -> new ShowtimeDTO(
                s.getId(),
                s.getStartTime(),
                s.getPrice(),
                s.getRoom().getId(),
                s.getRoom().getName(),
                s.getRoom().getCinema().getId(),
                s.getRoom().getCinema().getName(),
                s.getRoom().getCinema().getAddress()
        )).collect(Collectors.toList());
    }

    // 3. Sơ đồ ghế: ghế nào đã đặt, ghế nào còn trống
    @Override
    @Transactional(readOnly = true)
    public List<SeatStatusDTO> getSeatsByShowtimeId(Integer showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu ID: " + showtimeId));

        Integer roomId = showtime.getRoom().getId();
        List<Seat> allSeats = seatRepository.findByRoomId(roomId);

        List<Integer> bookedSeatIds = bookingDetailRepository.findBookedSeatIdsByShowtimeId(showtimeId);
        Set<Integer> bookedSet = Set.copyOf(bookedSeatIds);

        return allSeats.stream().map(seat -> {
            String seatType = seat.getSeatNumber().startsWith("V") ? "VIP" : "STANDARD";
            return new SeatStatusDTO(
                    seat.getId(),
                    seat.getSeatNumber(),
                    seatType,
                    bookedSet.contains(seat.getId())
            );
        }).collect(Collectors.toList());
    }

    // 4. Lấy chi tiết 1 suất chiếu
    @Override
    public Showtime getById(Integer id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu ID: " + id));
    }

    // 5. Admin: Thêm suất chiếu mới (check trùng lịch)
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

    // 6. Admin: Cập nhật suất chiếu
    @Override
    public Showtime updateShowtime(Integer id, Showtime showtime) {
        Showtime existing = getById(id);

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

    // 7. Admin: Xóa suất chiếu
    @Override
    public void deleteShowtime(Integer id) {
        showtimeRepository.deleteById(id);
    }
}

