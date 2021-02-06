package test;

import exceptions.DuplicateReviewException;
import exceptions.ReviewOfNotYetReleaseMovieException;
import models.Movie;
import models.Review;
import models.User;
import org.junit.jupiter.api.*;
import services.MovieService;
import services.ReviewService;
import services.UserService;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MovieReviewTest {

    private static List<UUID> userIds;

    @BeforeAll
    public static void init() {
        populateMovies();
        userIds = populateUsers();
    }

    @Test
    @DisplayName("Check reviews are added without any exception")
    @Order(1)
    public void firstTest() throws ReviewOfNotYetReleaseMovieException, DuplicateReviewException {
        Review review = Review.builder()
                .user(userIds.get(0))
                .movie(MovieService.getMovie("Don-2006").getId())
                .score(6)
                .build();
        ReviewService.addReview(review);

        review = Review.builder()
                .user(userIds.get(0))
                .movie(MovieService.getMovie("The Lunchbox-2013").getId())
                .score(8)
                .build();
        ReviewService.addReview(review);

        review = Review.builder()
                .user(userIds.get(1))
                .movie(MovieService.getMovie("Padmaavat-2018").getId())
                .score(8)
                .build();
        ReviewService.addReview(review);
    }

    @Test
    @DisplayName("Check adding duplicate reviews and reviews of not released movies throws exception")
    @Order(2)
    public void secondTest() {
        Review review1 = Review.builder()
                .user(userIds.get(0))
                .movie(MovieService.getMovie("The Lunchbox-2013").getId())
                .score(7)
                .build();
        assertThrows(DuplicateReviewException.class, () -> ReviewService.addReview(review1));

        Review review2 = Review.builder()
                .user(userIds.get(2))
                .movie(MovieService.getMovie("Bell Bottom-2021").getId())
                .score(9)
                .build();
        assertThrows(ReviewOfNotYetReleaseMovieException.class, () -> ReviewService.addReview(review2));
    }

    @Test
    @DisplayName("Check a user is upgraded to critic after 3 reviews")
    @Order(3)
    public void thirdTest() throws ReviewOfNotYetReleaseMovieException, DuplicateReviewException {
        Review review = Review.builder()
                .user(userIds.get(0))
                .movie(MovieService.getMovie("Guru-2007").getId())
                .score(9)
                .build();
        ReviewService.addReview(review);

        review = Review.builder()
                .user(userIds.get(0))
                .movie(MovieService.getMovie("Bhaag Milkha Bhaag-2013").getId())
                .score(8)
                .build();
        ReviewService.addReview(review);

        assertTrue(UserService.checkUserLevel(userIds.get(0), User.Level.CRITIC));
    }

    @Test
    @DisplayName("Check accuracy of average score for a movie")
    @Order(4)
    public void fourthTest() throws ReviewOfNotYetReleaseMovieException, DuplicateReviewException {
        Review review = Review.builder()
                .user(userIds.get(1))
                .movie(MovieService.getMovie("Guru-2007").getId())
                .score(7)
                .build();
        ReviewService.addReview(review);

        review = Review.builder()
                .user(userIds.get(2))
                .movie(MovieService.getMovie("Guru-2007").getId())
                .score(9)
                .build();
        ReviewService.addReview(review);

        double avgScore = MovieService.avgMovieReview(MovieService.getMovie("Guru-2007").getId());

        assertEquals(8.33, avgScore);
    }

    @Test
    @DisplayName("Check accuracy of average score in a particular year of release")
    @Order(5)
    public void fifthTest() throws ReviewOfNotYetReleaseMovieException, DuplicateReviewException {
        Review review = Review.builder()
                .user(userIds.get(1))
                .movie(MovieService.getMovie("Queen-2013").getId())
                .score(7)
                .build();
        ReviewService.addReview(review);

        review = Review.builder()
                .user(userIds.get(3))
                .movie(MovieService.getMovie("Bhaag Milkha Bhaag-2013").getId())
                .score(8)
                .build();
        ReviewService.addReview(review);

        double avgScore = ReviewService.avgReviewScore(2013);

        assertEquals(9.75, avgScore);
    }

    @Test
    @DisplayName("Check top N movies by total review score by ‘critics’ in a genre")
    @Order(6)
    public void sixthTest() throws ReviewOfNotYetReleaseMovieException, DuplicateReviewException {
        Review review = Review.builder()
                .user(userIds.get(1))
                .movie(MovieService.getMovie("Baby-2015").getId())
                .score(7)
                .build();
        ReviewService.addReview(review);

        review = Review.builder()
                .user(userIds.get(1))
                .movie(MovieService.getMovie("Dangal-2016").getId())
                .score(7)
                .build();
        ReviewService.addReview(review);

        List<Movie> topN = MovieService.topNMovies(2, "Biography");

        assertEquals(2, topN.size());
        assertTrue(topN.stream().anyMatch(movie -> movie.getTitle().equals("Guru")));
        assertTrue(topN.stream().anyMatch(movie -> movie.getTitle().equals("Bhaag Milkha Bhaag")));
    }

    private static void populateMovies() {
        Movie movie = Movie.builder()
                .title("Don")
                .releaseYear(2006)
                .genres(new HashSet<>(Arrays.asList("Action", "Crime", "Thriller")))
                .build();
        MovieService.addMovie(movie);

        movie = Movie.builder()
                .title("Baby")
                .releaseYear(2015)
                .genres(new HashSet<>(Arrays.asList("Action", "Thriller")))
                .build();
        MovieService.addMovie(movie);

        movie = Movie.builder()
                .title("Padmaavat")
                .releaseYear(2018)
                .genres(new HashSet<>(Arrays.asList("Drama", "History", "Romance", "War")))
                .build();
        MovieService.addMovie(movie);

        movie = Movie.builder()
                .title("The Lunchbox")
                .releaseYear(2013)
                .genres(new HashSet<>(Arrays.asList("Drama", "Romance")))
                .build();
        MovieService.addMovie(movie);

        movie = Movie.builder()
                .title("Dangal")
                .releaseYear(2016)
                .genres(new HashSet<>(Arrays.asList("Action", "Biography", "Drama")))
                .build();
        MovieService.addMovie(movie);

        movie = Movie.builder()
                .title("Guru")
                .releaseYear(2007)
                .genres(new HashSet<>(Arrays.asList("Biography", "Drama")))
                .build();
        MovieService.addMovie(movie);

        movie = Movie.builder()
                .title("Queen")
                .releaseYear(2013)
                .genres(new HashSet<>(Arrays.asList("Adventure", "Comedy", "Drama")))
                .build();
        MovieService.addMovie(movie);

        movie = Movie.builder()
                .title("Bhaag Milkha Bhaag")
                .releaseYear(2013)
                .genres(new HashSet<>(Arrays.asList("Biography", "Drama", "Sport")))
                .build();
        MovieService.addMovie(movie);

        movie = Movie.builder()
                .title("Bell Bottom")
                .releaseYear(2021)
                .genres(new HashSet<>(Arrays.asList("Action", "Thriller")))
                .build();
        MovieService.addMovie(movie);

        movie = Movie.builder()
                .title("The Girl on the Train")
                .releaseYear(2021)
                .genres(new HashSet<>(Arrays.asList("Crime", "Drama", "Mystery")))
                .build();
        MovieService.addMovie(movie);

    }

    private static List<UUID> populateUsers() {
        UserService.addUser(new User("Ayush"));
        UserService.addUser(new User("Bob"));
        UserService.addUser(new User("Daman"));
        UserService.addUser(new User("Prashant"));
        UserService.addUser(new User("Shruti"));

        return UserService.getUsers().collect(Collectors.toList()).stream().map(User::getId).collect(Collectors.toList());
    }
}