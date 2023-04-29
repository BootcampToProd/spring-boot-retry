package com.bootcamptoprod.retry.controller.mock;

import com.bootcamptoprod.retry.entity.Movie;
import com.bootcamptoprod.retry.exception.MovieNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock/movies")
public class MockMovieController {

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable String id) {
        if (id.equals("1")) {
            return new Movie("1", "The Matrix", "Lana Wachowski, Lilly Wachowski", 8.7);
        } else if (id.equals("2")) {
            return new Movie("2", "Inception", "Christopher Nolan", 8.8);
        } else {
            throw new MovieNotFoundException("Movie with id " + id + " not found.");
        }
    }
}

