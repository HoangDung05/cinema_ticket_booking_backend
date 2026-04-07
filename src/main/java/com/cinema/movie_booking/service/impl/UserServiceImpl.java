package com.cinema.movie_booking.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cinema.movie_booking.entity.User;
import com.cinema.movie_booking.repository.UserRepository;
import com.cinema.movie_booking.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllCustomers() {
        return userRepository.findByRoleName("CUSTOMER");
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateProfile(Integer id, User updatedUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existing.setFullName(updatedUser.getFullName());
        existing.setPhone(updatedUser.getPhone());
        return userRepository.save(existing);
    }
}