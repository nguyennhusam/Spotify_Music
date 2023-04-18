package hcmute.edu.vn.musicmediaplayer.TabFragmentHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import hcmute.edu.vn.musicmediaplayer.Activity.GenresActivity;
import hcmute.edu.vn.musicmediaplayer.R;

public class GenresFragment extends Fragment {

    LinearLayout layoutIndieMusic;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public GenresFragment() {
        // Required empty public constructor
    }

    public static GenresFragment newInstance(String param1, String param2) {
        GenresFragment fragment = new GenresFragment();
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
        View view = inflater.inflate(R.layout.fragment_genres, container, false);

        layoutIndieMusic = view.findViewById(R.id.layout_nhacindie);
        layoutIndieMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GenresActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}