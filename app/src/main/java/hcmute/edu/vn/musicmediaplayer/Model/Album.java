package hcmute.edu.vn.musicmediaplayer.Model;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private String id;
    private String name;
    private int totalSong;
    private String description;
    private String albumImageUrl;

    private List<Song> songs;



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getTotalSong() {
        return totalSong;
    }

    public void setTotalSong(int totalSong) {
        this.totalSong = totalSong;
    }


    public String getAlbumImageUrl() {
        return albumImageUrl;
    }

    public void setAlbumImageUrl(String albumImageUrl) {
        this.albumImageUrl = albumImageUrl;
    }



    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Album() {
        songs = new ArrayList<>();
    }


    public  void addSong(Song song){
        totalSong+=1;
        songs.add(song);
    }


    // creating constructor on below line.

}
