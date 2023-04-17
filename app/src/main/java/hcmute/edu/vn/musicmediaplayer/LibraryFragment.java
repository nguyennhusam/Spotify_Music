package hcmute.edu.vn.musicmediaplayer;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hcmute.edu.vn.musicmediaplayer.Adapter.AlbumAdapter;
import hcmute.edu.vn.musicmediaplayer.Model.Album;
import hcmute.edu.vn.musicmediaplayer.Model.Song;

public class LibraryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button btnUpload, btnAddAlbum;

    private String mParam1;
    private String mParam2;

    private RelativeLayout liked_album;
    private RelativeLayout available_album;

    private DatabaseReference mDatabaseRef;



    public LibraryFragment() {
        // Required empty public constructor
    }


    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v= inflater.inflate(R.layout.fragment_library, container, false);
        // Inflate the layout for this fragment
        RecyclerView albumsRV = v.findViewById(R.id.id_RCVAlbum);

        btnUpload = v.findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUploadActivity();
            }
        });

        liked_album = v.findViewById(R.id.liked_album);
        available_album = v.findViewById(R.id.available_album);
        btnAddAlbum = v.findViewById(R.id.btn_add_album);



        btnAddAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewPlaylistDialogFragment dialog = new NewPlaylistDialogFragment();
                dialog.show(getFragmentManager(), "NewPlaylistDialogFragment");
            }
        });

        liked_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getContext(),AlbumDetailsActivity.class);
//                intent.putExtras();
                startActivity(intent);
            }
        });

        available_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getContext(),AlbumDetailsActivity.class);
//                intent.putExtras();
                startActivity(intent);
            }
        });

        // Read list album from database
        mDatabaseRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("albums");
        ArrayList<Album> albumRVModalArrayList = new ArrayList<>();
        FragmentManager  fragmentManager = getChildFragmentManager();
        AlbumAdapter albumRVAdapter = new AlbumAdapter(albumRVModalArrayList, fragmentManager);
        albumsRV.setHasFixedSize(true);
        albumsRV.setAdapter(albumRVAdapter);

//        final boolean[] isFirstTime = {true};
//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Album album = dataSnapshot.getValue(Album.class);
//                    albumRVModalArrayList.add(album);
//                }
//                albumRVAdapter.notifyDataSetChanged();
//
////                if (isFirstTime[0]) {
////                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
////                        Album album = dataSnapshot.getValue(Album.class);
////                        albumRVModalArrayList.add(album);
////                    }
////                    albumRVAdapter.notifyDataSetChanged();
////                    isFirstTime[0] = false;
////                } else {
////                    Album newAlbum = snapshot.getValue(Album.class);
////                    albumRVModalArrayList.add(newAlbum);
////                    int insertedPosition = albumRVModalArrayList.size() - 1;
////                    albumRVAdapter.notifyItemRangeInserted(insertedPosition, 1);
////                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//
//        });
        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Album album = snapshot.getValue(Album.class);
                albumRVModalArrayList.add(album);

                albumRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }

    private void initializeAlbumsRV() {
        // on below line initializing albums rv
//        RecyclerView albumsRV = findViewById(R.id.id_RCVAlbum);
//        // on below line creating list, initializing adapter
//        // and setting it to recycler view.
//        ArrayList<Albums> albumRVModalArrayList = new ArrayList<>();
//
//        albumRVModalArrayList.add(new Albums(1,"Sẵn trên máy",15,"https://cdn-icons-png.flaticon.com/512/31/31623.png"));
//        albumRVModalArrayList.add(new Albums(2,"Yêu thích",10,"https://cdn-icons-png.flaticon.com/512/1077/1077035.png"));
//        albumRVModalArrayList.add(new Albums(3,"Tập của bạn",5,"https://cdn-icons-png.flaticon.com/512/25/25667.png"));
//
//        AlbumAdapter albumRVAdapter = new AlbumAdapter(albumRVModalArrayList, this);
//        albumsRV.setAdapter(albumRVAdapter);
//        // on below line creating a variable for url
//        String url = "https://api.spotify.com/v1/albums?ids=2oZSF17FtHQ9sYBscQXoBe%2C0z7bJ6UpjUw8U4TATtc5Ku%2C36UJ90D0e295TvlU109Xvy%2C3uuu6u13U0KeVQsZ3CZKK4%2C45ZIondgVoMB84MQQaUo9T%2C15CyNDuGY5fsG0Hn9rjnpG%2C1HeX4SmCFW4EPHQDvHgrVS%2C6mCDTT1XGTf48p6FkK9qFL";
//
//        albumRVAdapter.notifyDataSetChanged();
        //        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        // on below line making json object request to parse json data.
//        JsonObjectRequest albumObjReq = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray albumArray = response.getJSONArray("albums");
//                    for (int i = 0; i < albumArray.length(); i++) {
//                        JSONObject albumObj = albumArray.getJSONObject(i);
//                        String album_type = albumObj.getString("album_type");
//                        String artistName = albumObj.getJSONArray("artists").getJSONObject(0).getString("name");
//                        String external_ids = albumObj.getJSONObject("external_ids").getString("upc");
//                        String external_urls = albumObj.getJSONObject("external_urls").getString("spotify");
//                        String href = albumObj.getString("href");
//                        String id = albumObj.getString("id");
//                        String imgUrl = albumObj.getJSONArray("images").getJSONObject(1).getString("url");
//                        String label = albumObj.getString("label");
//                        String name = albumObj.getString("name");
//                        int popularity = albumObj.getInt("popularity");
//                        String release_date = albumObj.getString("release_date");
//                        int total_tracks = albumObj.getInt("total_tracks");
//                        String type = albumObj.getString("type");
//                        // on below line adding data to array list.
//                        albumRVModalArrayList.add(new AlbumRVModal(album_type, artistName, external_ids, external_urls, href, id, imgUrl, label, name, popularity, release_date, total_tracks, type));
//                    }
//                    albumRVAdapter.notifyDataSetChanged();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this, "Fail to get data : " + error, Toast.LENGTH_SHORT).show();
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                // on below line passing headers.
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Authorization", getToken());
//                headers.put("Accept", "application/json");
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
        // on below line adding request to queue.
//        queue.add(albumObjReq);
    }
    public void openUploadActivity() {
        Intent intent = new Intent(getActivity(), UploadActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Toast.makeText(getActivity(), "File uploaded successfully", Toast.LENGTH_SHORT).show();
        }
    }


}