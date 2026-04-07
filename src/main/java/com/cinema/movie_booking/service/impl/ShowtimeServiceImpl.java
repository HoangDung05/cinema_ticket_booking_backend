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

    @Override
    public List<Showtime> getShowtimesByMovieId(Integer movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }

    // --- API 1: GET /api/movies/{movieId}/showtimes ---
    @Override
    @Transactional(readOnly = true)
    public List<ShowtimeDTO> getShowtimeDTOsByMovieId(Integer movieId) {
        // Lấy danh sách suất chiếu từ thời điểm hiện tại trở đi
        List<Showtime> showtimes = showtimeRepository.findByMovieIdAndStartTimeAfter(movieId, LocalDateTime.now());

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

    // --- API 2: GET /api/showtimes/{id}/seats ---
    @Override
    @Transactional(readOnly = true)
    public List<SeatStatusDTO> getSeatsByShowtimeId(Integer showtimeId) {
        // Lấy suất chiếu để biết phòng chiếu
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy suất chiếu có ID: " + showtimeId));

        Integer roomId = showtime.getRoom().getId();

        // Lấy tất cả ghế trong phòng chiếu đó
        List<Seat> allSeats = seatRepository.findByRoomId(roomId);

        // Lấy danh sách ID các ghế đã được đặt trong suất chiếu này
        List<Integer> bookedSeatIds = bookingDetailRepository.findBookedSeatIdsByShowtimeId(showtimeId);
        Set<Integer> bookedSet = Set.copyOf(bookedSeatIds);

        // Map từng ghế ra SeatStatusDTO, đánh dấu ghế nào đã bị đặt
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
}
