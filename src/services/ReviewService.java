package services;

import exceptions.DuplicateReviewException;
import exceptions.ReviewOfNotYetReleaseMovieException;
import models.Movie;
import models.Review;
import models.User;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReviewService {

    private static Map<UUID, Review> reviews = new HashMap<>();

    public static Stream<Review> getReviews() {
        return reviews.values().stream();
    }

    public static void addReview(Review newReview) throws DuplicateReviewException, ReviewOfNotYetReleaseMovieException {
        long countReviews = 1;
        for (Map.Entry<UUID, Review> entry : reviews.entrySet()) {
            Review review = entry.getValue();
            if (newReview.getUser() == review.getUser()) {
                countReviews++;
            }
            if (newReview.getMovie() == review.getMovie() && newReview.getUser() == review.getUser()) {
                throw new DuplicateReviewException("multiple reviews not allowed", newReview.getUser(), newReview.getMovie());
            }
            if(MovieService.getMovie(newReview.getMovie()).getReleaseYear() >= Calendar.getInstance().get(Calendar.YEAR)) {
                throw new ReviewOfNotYetReleaseMovieException("movie not yet released", newReview.getMovie());
            }
        }
        reviews.put(newReview.getId(), newReview);
        if(countReviews == 4) {
            UserService.upgradeUserLevel(newReview.getUser());
        }
        if(UserService.checkUserLevel(newReview.getUser(), User.Level.CRITIC)) {
            newReview.setScore(2 * newReview.getScore());
        }
    }

    public static List<Review> getMovieReviews(UUID movieId) {
        return getReviews().filter(review -> review.getMovie() == movieId).collect(Collectors.toList());
    }

    public static double avgReviewScore(int year) {
        Set<UUID> movies = MovieService.getMovies().filter(movie -> movie.getReleaseYear() == year).map(Movie::getId).collect(Collectors.toSet());
        double avgScore = getReviews().filter(review -> movies.contains(review.getMovie()))
                .mapToDouble(Review::getScore)
                .average()
                .orElse(Double.NaN);
        return (double)Math.round(avgScore * 100) / 100;
    }
}
