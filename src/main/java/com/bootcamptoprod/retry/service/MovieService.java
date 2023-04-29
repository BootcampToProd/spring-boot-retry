package com.bootcamptoprod.retry.service;

import com.bootcamptoprod.retry.entity.Movie;
import com.bootcamptoprod.retry.rest.client.MovieApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

@Service
public class MovieService {

    @Autowired
    private MovieApiClient movieApiClient;

    @Retryable(
            retryFor = {HttpServerErrorException.class, HttpClientErrorException.class, ResourceAccessException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 5000))
    public Movie getMovieDetails(String movieId) throws ResourceAccessException {
        Movie movie = null;
        try {
            movie = movieApiClient.getMovieDetails(movieId);
        } catch (HttpServerErrorException httpServerErrorException) {
            System.out.println("Received HTTP server error exception while fetching the movie details. Error Message: " + httpServerErrorException.getMessage());
            throw httpServerErrorException;
        } catch (HttpClientErrorException httpClientErrorException) {
            System.out.println("Received HTTP client error exception while fetching the movie details. Error Message: " + httpClientErrorException.getMessage());
            throw httpClientErrorException;
        } catch (ResourceAccessException resourceAccessException) {
            System.out.println("Received Resource Access exception while fetching the movie details.");
            throw resourceAccessException;
        }
        return movie;
    }

    @Recover
    public Movie recoverMovieDetails(RestClientException e) {
        // Log the error
        System.out.println("Error fetching movie details: " + e.getMessage());

        // Return a default movie
        return new Movie("0000", "Default movie", "Unknown", 0.0);
    }
}
