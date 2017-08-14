package com.example.alexander.popularmoviesapp.moviedata;

/**
 * Created by awest on 8/11/2017.
 */

public class Review implements JsonDataType {
    private String author;
    private String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getJsonDataType() {
        return "Review";
    }

    @Override
    public String toString() {
        return "Review - " +
                "Author: " + author + " " +
                "Content: " + content;
    }
}
