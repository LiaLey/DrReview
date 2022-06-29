package com.example.drreview.items;

//stores the data for the review items object
public class ReviewItem {

    String user;
    String reviews;

    //empty constructor needed for firebase
    public ReviewItem(){ }

    public ReviewItem(String user, String reviews) {
        this.user = user;
        this.reviews = reviews;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }
}
