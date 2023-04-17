package hcmute.edu.vn.musicmediaplayer.TabFragmentHome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import hcmute.edu.vn.musicmediaplayer.MainActivity;
import hcmute.edu.vn.musicmediaplayer.Model.Song;
import hcmute.edu.vn.musicmediaplayer.R;

public class SingersFragment extends Fragment {
    LinearLayout layoutMono;

    CircleImageView imageView1;
    CircleImageView imageView2;
    CircleImageView imageView3;
    CircleImageView imageView4;
    CircleImageView imageView5;
    CircleImageView imageView6;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SingersFragment() {
        // Required empty public constructor
    }

    public static SingersFragment newInstance(String param1, String param2) {
        SingersFragment fragment = new SingersFragment();
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
        View view = inflater.inflate(R.layout.fragment_singers, container, false);

        CircleImageView imageView1 = view.findViewById(R.id.image_view_sontung);
        Glide.with(this)
                .load(R.drawable.sontungmpt)
                .into(imageView1);

        CircleImageView imageView2 = view.findViewById(R.id.image_view_mono);
        Glide.with(this)
                .load(R.drawable.mono)
                .into(imageView2);

        CircleImageView imageView3 = view.findViewById(R.id.image_view_hoangdung);
        Glide.with(this)
                .load(R.drawable.hoangdung)
                .into(imageView3);

        CircleImageView imageView4 = view.findViewById(R.id.image_view_justinbieber);
        Glide.with(this)
                .load(R.drawable.justinbieber)
                .into(imageView4);

        CircleImageView imageView5 = view.findViewById(R.id.image_view_taylorswift);
        Glide.with(this)
                .load(R.drawable.taylorswift)
                .into(imageView5);

        CircleImageView imageView6 = view.findViewById(R.id.image_view_charlieputh);
        Glide.with(this)
                .load(R.drawable.charlieputh)
                .into(imageView6);





        return view;
    }
}