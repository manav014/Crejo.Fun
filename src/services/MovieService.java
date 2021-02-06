package services;

import models.Movie;
import models.Review;
import models.User;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class MovieService {

    private static Map<UUID, Movie> movies = new HashMap<>();
    private static Map<String, Movie> moviesTitleWithYearIndex = new HashMap<>();

    public static Stream<Movie> getMovies() {
        return movies.values().stream();
    }

    public static void addMovie(Movie newMovie) {
        String titleWithYear = newMovie.getTitle() + "-" + newMovie.getReleaseYear();
        if(moviesTitleWithYearIndex.containsKey(titleWithYear)) {
            return;
        }
        moviesTitleWithYearIndex.put(titleWithYear, newMovie);
        movies.put(newMovie.getId(), newMovie);
    }

    public static Movie getMovie(UUID id) {
        return movies.get(id);
    }

    public static Movie getMovie(String titleWithYear) {
        return moviesTitleWithYearIndex.get(titleWithYear);
    }

    public static List<Movie> getMovies(String genre) {
        return  getMovies().filter(movie -> movie.getGenres().contains(genre)).collect(Collectors.toList());
    }

    public static double avgMovieReview(UUID movieId) {
        List<Review> movieReviews = ReviewService.getMovieReviews(movieId);
        double avgScore = movieReviews.stream()
                .mapToDouble(Review::getScore)
                .average()
                .orElse(Double.NaN);
        return (double)Math.round(avgScore * 100) / 100;
    }

    public static List<Movie> topNMovies(int n, String genre) {
        Set<UUID> movieSet = getMovies(genre).stream().map(Movie::getId).collect(Collectors.toSet());
        Set<UUID> topNMovies = ReviewService.getReviews()
                        .filter(review -> UserService.checkUserLevel(review.getUser(), User.Level.CRITIC) && movieSet.contains(review.getMovie()))
                        .collect(groupingBy(Review::getMovie, summingLong(Review::getScore)))
                        .entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                        .limit(n)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet());

        return topNMovies.stream().map(MovieService::getMovie).collect(Collectors.toList());
    }
}
