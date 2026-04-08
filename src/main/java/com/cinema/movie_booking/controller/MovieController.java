package com.cinema.movie_booking.controller;

import com.cinema.movie_booking.entity.Movie;
import com.cinema.movie_booking.entity.MovieStatus;
import com.cinema.movie_booking.entity.Showtime;
import com.cinema.movie_booking.service.MovieService;
import com.cinema.movie_booking.service.ShowtimeService;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final ShowtimeService showtimeService;

    // 1. API LẤY DANH SÁCH & LỌC

    @GetMapping
    public ResponseEntity<List<Movie>> getAllOrFilterMovies(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) MovieStatus status) {

        List<Movie> movies = movieService.filterMovies(date, status);
        return ResponseEntity.ok(movies);
    }

    // 2. API LẤY CHI TIẾT 1 PHIM THEO ID

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Integer id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    // 3. API THÊM PHIM MỚI

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.saveMovie(movie);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    // 4. API CẬP NHẬT PHIM

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @RequestBody Movie movieDetails) {

        Movie existingMovie = movieService.getMovieById(id);

        existingMovie.setTitle(movieDetails.getTitle());
        existingMovie.setDescription(movieDetails.getDescription());
        existingMovie.setDuration(movieDetails.getDuration());
        existingMovie.setReleaseDate(movieDetails.getReleaseDate());
        existingMovie.setPosterUrl(movieDetails.getPosterUrl());
        existingMovie.setTrailerUrl(movieDetails.getTrailerUrl());
        existingMovie.setStatus(movieDetails.getStatus());

        Movie updatedMovie = movieService.saveMovie(existingMovie);
        return ResponseEntity.ok(updatedMovie);
    }

    // 5. API XÓA PHIM

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Xóa phim thành công!");
    // 3. Lấy phim đang chiếu
    @GetMapping("/now-showing")
    public ResponseEntity<List<Movie>> getNowShowing() {
        return ResponseEntity.ok(movieService.getNowShowingMovies());
    }

    // 4. Lấy phim sắp chiếu
    @GetMapping("/coming-soon")
    public ResponseEntity<List<Movie>> getComingSoon() {
        return ResponseEntity.ok(movieService.getComingSoonMovies());
    }

    // Lấy các suất chiếu của một bộ phim cụ thể (Để vẽ bảng chọn giờ cho khách)
    @GetMapping("/{movieId}/showtimes")
    public ResponseEntity<List<Showtime>> getShowtimesByMovie(@PathVariable Integer movieId) {
        List<Showtime> showtimes = showtimeService.getShowtimesByMovieId(movieId);
        return ResponseEntity.ok(showtimes);
    }
}