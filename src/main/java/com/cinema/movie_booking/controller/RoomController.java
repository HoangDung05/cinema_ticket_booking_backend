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

    // 3. Tạo phòng mới cho 1 rạp
    @PostMapping("/cinema/{cinemaId}")
    public ResponseEntity<Room> create(@PathVariable Integer cinemaId, @RequestBody Room room) {
        return ResponseEntity.ok(roomService.createRoom(cinemaId, room));
    }

    // 4. Xóa phòng
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Đã xóa phòng thành công!");
    }
}