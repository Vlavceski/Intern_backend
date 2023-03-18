package com.intern.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Filters {
    private String prioritizeByText;
    private String orderByRating;
    private String orderByDate;
    private int minimumRating;

    public Filters() {
    }

    public Filters(String prioritizeByText, String orderByRating, String orderByDate, int minimumRating) {
        this.prioritizeByText = prioritizeByText;
        this.orderByRating = orderByRating;
        this.orderByDate = orderByDate;
        this.minimumRating = minimumRating;
    }
}