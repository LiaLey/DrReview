package com.example.drreview.items;

//store the data of the hospital object
public class DoctorItems {

    String doctor;

    //Empty constructor needed for Firebase
    public DoctorItems(){

    }

    public DoctorItems(String doctor) {
        this.doctor = doctor;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
