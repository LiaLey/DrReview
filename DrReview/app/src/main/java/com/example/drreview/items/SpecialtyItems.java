package com.example.drreview.items;

//store the data of the specialty object
public class SpecialtyItems {

    String specialty;

    //Empty constructor needed for Firebase
    public SpecialtyItems(){ }

    public SpecialtyItems(String specialty) {
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
