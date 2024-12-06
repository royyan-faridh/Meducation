package com.example.meduction.consultation.models;

public class Doctor {
    private String name;
    private String specialty;
    private String price;
    private double rating;
    private int image;

    // Constructor
    public Doctor(String name, String specialty, String price, double rating, int image) {
        this.name = name;
        this.specialty = specialty;
        this.price = price;
        this.rating = rating;
        this.image = image;
    }

    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
