package com.example.meduction;

public class Item {
    private String title;
    private String description;
    private String materialCount;  // Mengubah tipe menjadi String
    private String imageUrl;

    // Konstruktor
    public Item(String title, String description, String materialCount, String imageUrl) {
        this.title = title;
        this.description = description;
        this.materialCount = materialCount;
        this.imageUrl = imageUrl;
    }

    // Getter dan Setter untuk setiap variabel
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialCount() {
        return materialCount;
    }

    public void setMaterialCount(String materialCount) {
        this.materialCount = materialCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
