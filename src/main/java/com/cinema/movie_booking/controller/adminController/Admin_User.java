package com.cinema.movie_booking.controller.adminController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cinema.movie_booking.entity.User;
import com.cinema.movie_booking.service.UserService;

import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/admin")
@RequiredArgsConstructor
public class Admin_User {
    private final UserService userService;

    // PI Quản lý danh sách khách hàng

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers() {
        return ResponseEntity.ok(userService.getAllCustomers());
    }

    // Lấy tất cả user
    @GetMapping("/allusers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    // Cập nhật thông tin user

    @PutMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    // Xóa user
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Đã xóa khách hàng ID: " + id);
    }

    // Khóa tài khoản
    @PutMapping("/customers/{id}/status")
    public ResponseEntity<?> changeStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        try {
            User updatedUser = userService.updateUserStatus(id, status);
            return ResponseEntity.ok(
                    "Người dùng " + updatedUser.getFullName() + " hiện đang ở trạng thái: " + updatedUser.getStatus());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
