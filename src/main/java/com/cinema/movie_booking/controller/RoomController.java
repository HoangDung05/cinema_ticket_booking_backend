package com.cinema.movie_booking.controller;

import com.cinema.movie_booking.entity.Room;
import com.cinema.movie_booking.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // 6.0. Lấy danh sách tất cả các phòng
    @GetMapping("/allrooms")
    public ResponseEntity<?> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // 1. Lấy tất cả phòng của 1 rạp cụ thể
    @GetMapping("/cinema/{cinemaId}")
    public List<Room> getByCinema(@PathVariable Integer cinemaId) {
        return roomService.getRoomsByCinema(cinemaId);
    }

    // 2. Lấy chi tiết 1 phòng
    @GetMapping("/{id}")
    public ResponseEntity<Room> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

}