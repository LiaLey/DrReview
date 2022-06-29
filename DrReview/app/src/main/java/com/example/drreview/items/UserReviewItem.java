package com.example.drreview.items;

//stores the data for the review items object
public class UserReviewItem {

    String doctor, reviews;

    //empty constructor needed for firebase
    public UserReviewItem(){ }

    public UserReviewItem(String doctor, String reviews) {
        this.doctor = doctor;
        this.reviews = reviews;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }
}
