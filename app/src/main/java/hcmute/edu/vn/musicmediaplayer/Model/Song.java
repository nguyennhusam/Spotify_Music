package hcmute.edu.vn.musicmediaplayer.Model;



import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class Song implements Parcelable {


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



    protected Song(Parcel in) {
        songId = in.readString();
        sName = in.readString();
        sSongUrl = in.readString();
        sArtist = in.readString();
        sImageUrl = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(songId);
        parcel.writeString(sName);
        parcel.writeString(sSongUrl);
        parcel.writeString(sArtist);
        parcel.writeString(sImageUrl);
    }
}
