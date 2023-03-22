package hcmute.edu.vn.musicmediaplayer.Model;

public class Albums {
    private int id;
    private String name;
    private int total_tracks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal_tracks() {
        return total_tracks;
    }

    public void setTotal_tracks(int total_tracks) {
        this.total_tracks = total_tracks;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    private String picture;

    public Albums(int id, String name, int total_tracks, String picture) {
        this.id = id;
        this.name = name;
        this.total_tracks = total_tracks;
        this.picture = picture;
    }
    public Albums() {

    }


    // creating constructor on below line.

}
