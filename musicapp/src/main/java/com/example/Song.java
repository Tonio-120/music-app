package com.example;

public class Song {

    private String title;
    private String artist;
    private int year;
    private String genre;

    private boolean isFavorite = false;

    private String comments = "";

    private String filePath;

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFilePath() {
        return filePath;
    }
}