package com.example.meduction.community.models;

public class Reply {
    private String author;
    private String date;
    private String content;

    public Reply(String author, String date, String content) {
        this.author = author;
        this.date = date;
        this.content = content;
    }

    // Getters
    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }
}

