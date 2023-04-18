package hcmute.edu.vn.musicmediaplayer;

import android.app.SearchManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.musicmediaplayer.Adapter.SearchAdapter;
import hcmute.edu.vn.musicmediaplayer.Model.Album;
import hcmute.edu.vn.musicmediaplayer.Model.Song;

public class SearchFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference myRef;
    RecyclerView recyclerViewtim;

    androidx.appcompat.widget.Toolbar toolbar;

    SearchAdapter searchAdapter;

    ArrayList<Song> listSong;
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        System.out.println();
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerViewtim = view.findViewById(R.id.recyclerviewtimkiem);
        recyclerViewtim.setHasFixedSize(true);
        toolbar = view.findViewById(R.id.toolbartimkiem);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        myRef = FirebaseDatabase.getInstance("https://musicapp-694ed-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("hitSong");
        setHasOptionsMenu(true);
        recyclerViewtim.setLayoutManager(new LinearLayoutManager(getContext()));
        listSong = new ArrayList<>();
        searchAdapter = new SearchAdapter(getContext(), listSong);
        recyclerViewtim.setAdapter(searchAdapter);

        //thanh chắn ngăn cách giữa các item
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this.getContext(),DividerItemDecoration.VERTICAL);
        recyclerViewtim.addItemDecoration(itemDecoration);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Song song = dataSnapshot.getValue(Song.class);

                    listSong.add(song);
                }
                searchAdapter.setFullList(new ArrayList<>(listSong));
                searchAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.searchId);
        SearchView searchView = (SearchView) menuItem.getActionView();
        //tinh chỉnh searchView vào giữa toolbar
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        searchView.setLayoutParams(new Toolbar.LayoutParams(width, Toolbar.LayoutParams.WRAP_CONTENT));
        searchView.setGravity(Gravity.CENTER);

        // Đặt padding bên trái và bên phải cho ô tìm kiếm
        searchView.setPadding(0, 0, 20, 0);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchAdapter.getFilter().filter(query);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}

