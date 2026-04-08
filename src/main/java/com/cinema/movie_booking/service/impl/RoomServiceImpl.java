package com.cinema.movie_booking.service.impl;

import com.cinema.movie_booking.entity.Room;
import com.cinema.movie_booking.repository.RoomRepository;
import com.cinema.movie_booking.repository.CinemaRepository;
import com.cinema.movie_booking.service.RoomService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    public RoomServiceImpl(RoomRepository roomRepository, CinemaRepository cinemaRepository) {
        this.roomRepository = roomRepository;
        this.cinemaRepository = cinemaRepository;
    }

    @Override
    public Room getRoomById(Integer id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Phòng chiếu với ID: " + id));
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getRoomsByCinema(Integer cinemaId) {
        return roomRepository.findByCinemaId(cinemaId);
    }

    @Override
    public Room createRoom(Integer cinemaId, Room room) {
        var cinema = cinemaRepository.findById(cinemaId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Rạp phim ID: " + cinemaId));
        room.setCinema(cinema);
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Integer id, Room roomDetails) {
        // 1. Tìm phòng cũ trong DB
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với ID: " + id));

        // 2. Cập nhật các trường thông tin
        room.setName(roomDetails.getName());

        if (roomDetails.getCinema() != null) {
            room.setCinema(roomDetails.getCinema());
        }

        // 3. Lưu lại
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Integer id) {
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy phòng với ID: " + id + " để xóa!");
        }
        roomRepository.deleteById(id);
    }
}