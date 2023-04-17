package hcmute.edu.vn.musicmediaplayer.Model;

public class Song {

    private String songId;
    private String sName;
    private String sSongUrl;
    private String sArtist;
    private String sImageUrl;


    public Song() {
    }

    public Song(String sName, String sSongUrl, String sArtist, String sImageUrl) {
        if (sName.trim().equals("")) {
            sName = "No Name";
        }
        this.sName = sName;

        if (sArtist.trim().equals("")) {
            sArtist = "No Name";
        }
        this.sArtist = sArtist;

        this.sSongUrl = sSongUrl;
        if (sImageUrl.trim().equals("")) {
            sImageUrl = "https://images.macrumors.com/t/hi1_a2IdFGRGMsJ0x31SdD_IcRk=/1600x/article-new/2018/05/apple-music-note.jpg";
        }
        this.sImageUrl = sImageUrl;
    }


    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsSongUrl() {
        return sSongUrl;
    }

    public void setsSongUrl(String sSongUrl) {
        this.sSongUrl = sSongUrl;
    }

    public String getsArtist() {
        return sArtist;
    }

    public void setsArtist(String sArtist) {
        this.sArtist = sArtist;
    }

    public String getsImageUrl() {
        return sImageUrl;
    }

    public void setsImageUrl(String sImageUrl) {
        this.sImageUrl = sImageUrl;
    }
}
