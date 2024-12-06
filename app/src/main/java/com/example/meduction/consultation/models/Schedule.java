package com.example.meduction.consultation.models;

public class Schedule {
    private String date;  // Tanggal
    private String time;  // Waktu
    private boolean isAvailable;  // Ketersediaan (Tersedia atau Tidak)

    // Constructor
    public Schedule(String date, String time, boolean isAvailable) {
        this.date = date;
        this.time = time;
        this.isAvailable = isAvailable;
    }

    // Getter dan Setter
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}


