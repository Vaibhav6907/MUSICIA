package com.musicia.model;

public class Song {
    private int id;
    private String title;
    private int artistId;
    private String filePath;
    private String coverArt;
    private String genre;

    // Constructors
    public Song() {}

    public Song(String title, int artistId, String filePath, String coverArt, String genre) {
        this.title = title;
        this.artistId = artistId;
        this.filePath = filePath;
        this.coverArt = coverArt;
        this.genre = genre;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getArtistId() { return artistId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getCoverArt() { return coverArt; }
    public void setCoverArt(String coverArt) { this.coverArt = coverArt; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}