package com.musicia.dao;

import com.musicia.model.Song;
import com.musicia.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {
    public void addSong(Song song) throws Exception {
        String query = "INSERT INTO songs (title, artist_id, file_path, cover_art, genre) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, song.getTitle());
            stmt.setInt(2, song.getArtistId());
            stmt.setString(3, song.getFilePath());
            stmt.setString(4, song.getCoverArt());
            stmt.setString(5, song.getGenre());
            
            stmt.executeUpdate();
        }
    }

    public List<Song> getAllSongs() throws Exception {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT * FROM songs";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Song song = new Song();
                song.setId(rs.getInt("id"));
                song.setTitle(rs.getString("title"));
                song.setArtistId(rs.getInt("artist_id"));
                song.setFilePath(rs.getString("file_path"));
                song.setCoverArt(rs.getString("cover_art"));
                song.setGenre(rs.getString("genre"));
                songs.add(song);
            }
        }
        return songs;
    }

    public List<Song> getSongsByArtist(int artistId) throws Exception {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT * FROM songs WHERE artist_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, artistId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Song song = new Song();
                song.setId(rs.getInt("id"));
                song.setTitle(rs.getString("title"));
                song.setArtistId(rs.getInt("artist_id"));
                song.setFilePath(rs.getString("file_path"));
                song.setCoverArt(rs.getString("cover_art"));
                song.setGenre(rs.getString("genre"));
                songs.add(song);
            }
        }
        return songs;
    }

    public Song getSongById(int id) throws Exception {
        String query = "SELECT * FROM songs WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Song song = new Song();
                    song.setId(rs.getInt("id"));
                    song.setTitle(rs.getString("title"));
                    song.setArtistId(rs.getInt("artist_id"));
                    song.setFilePath(rs.getString("file_path"));
                    song.setCoverArt(rs.getString("cover_art"));
                    song.setGenre(rs.getString("genre"));
                    return song;
                }
            }
        }
        return null;
    }

    public void deleteSong(int id) throws Exception {
        String query = "DELETE FROM songs WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void updateSongInfo(int id, String title, String genre, String coverArt) throws Exception {
        String query = "UPDATE songs SET title = ?, genre = ?, cover_art = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, genre);
            stmt.setString(3, coverArt);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }
}