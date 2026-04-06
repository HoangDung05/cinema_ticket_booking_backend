package com.cinema.movie_booking.service;

import java.util.List;

import com.cinema.movie_booking.entity.User;

public interface UserService {
    List<User> getAllCustomers();

    User updateProfile(Integer id, User user);

    User getById(Integer id);
}