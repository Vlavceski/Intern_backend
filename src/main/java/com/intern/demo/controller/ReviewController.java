package com.intern.demo.controller;

import com.google.gson.Gson;
import com.intern.demo.model.Review;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class ReviewController {
    private List<Review> reviews;
    //     http://localhost:8080/reviews?prioritizeByText=Yes&orderByRating=Highest_First&orderByDate=Highest_First&minRating=4
    @GetMapping("/reviews")
    public List<Review> getFilteredReviews(
            @RequestParam(value = "prioritizeByText", defaultValue = "Yes") String prioritizeByText,
            @RequestParam(value = "orderByRating", defaultValue = "Highest_First") String orderByRating,
            @RequestParam(value = "orderByDate", defaultValue = "Oldest_First") String orderByDate,
            @RequestParam(value = "minRating", defaultValue = "3") int minRating
    ) {
        System.out.println( prioritizeByText+" "+ orderByRating+ " "+orderByDate+ " "+ minRating);
        System.out.println("____________________");
        List<Review> reviews = readReviewsJsonFile();
        boolean prioritize=true;
        if (Objects.equals(prioritizeByText, "No")){
            prioritize= false;
        }
        // Filter by minimum rating
        reviews = reviews.stream().filter(review -> review.getRating() >= minRating).collect(Collectors.toList());

        // Sort by rating
        if (orderByRating.equalsIgnoreCase("Highest_First")) {
            reviews.sort(Comparator.comparing(Review::getRating).reversed());
        } else {
            reviews.sort(Comparator.comparing(Review::getRating));
        }

        // Sort by date
        if (orderByDate.equalsIgnoreCase("Highest_First")) {
            reviews.sort(Comparator.comparing(Review::getReviewCreatedOnDate));
        } else {
            reviews.sort(Comparator.comparing(Review::getReviewCreatedOnDate).reversed());
        }
        if (prioritize) {
            reviews.sort(Comparator.comparing(Review::getReviewText, Comparator.nullsLast(String::compareToIgnoreCase)).reversed());
        }
        System.out.println("_________________________________");

        for (Review review : reviews) {
            if (!Objects.equals(review.getReviewFullText(), "")) {
                System.out.println(review.getRating() + "-star reviews with text - " + orderByDate);
            }
            if (Objects.equals(review.getReviewFullText(), "")) {
                System.out.println(review.getRating() + "-star reviews without text - " + orderByDate);
            }
        }
        return null;
    }

    private List<Review> readReviewsJsonFile() {
        Gson gson = new Gson();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader("src/main/resources/review.json"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String json = bufferedReader.lines().reduce("", (accumulator, actual) -> accumulator + actual);
        Object object = gson.fromJson(json, Object.class);
        Review[] reviews = gson.fromJson(gson.toJson(object), Review[].class);
        return List.of(reviews);
    }

}