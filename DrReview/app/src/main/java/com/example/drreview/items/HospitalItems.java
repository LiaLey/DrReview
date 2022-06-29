package com.example.drreview.items;

//store the data of the hospital object
public class HospitalItems {

    String hosp;

    //Empty constructor needed for Firebase
    public HospitalItems(){ }

    public HospitalItems(String hosp) {
        this.hosp = hosp;
    }

    public String getHosp() {
        return hosp;
    }

    public void setHosp(String hosp) {
        this.hosp = hosp;
    }
}
