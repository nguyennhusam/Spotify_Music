package hcmute.edu.vn.musicmediaplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.musicmediaplayer.Model.Photo;
import me.relex.circleindicator.CircleIndicator3;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ViewPager2 imgSliderViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private List<Photo> mListPhoto;

    private Handler mHandler = new Handler();
    private Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            if(imgSliderViewPager2.getCurrentItem() == mListPhoto.size() - 1) {
                imgSliderViewPager2.setCurrentItem(0);
            } else {
                imgSliderViewPager2.setCurrentItem(imgSliderViewPager2.getCurrentItem() + 1);
            }
        }
    };

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        imgSliderViewPager2 = v.findViewById(R.id.img_slider_viewPager2);
        mCircleIndicator3 = v.findViewById(R.id.img_slider_circleIndicator3);


        mListPhoto = getListPhoto();
        PhotoAdapter adapter = new PhotoAdapter(mListPhoto);
        imgSliderViewPager2.setAdapter(adapter);

        mCircleIndicator3.setViewPager(imgSliderViewPager2);

        imgSliderViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunable);
                mHandler.postDelayed(mRunable,3000);
            }
        });

        return v;
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img1));
        list.add(new Photo(R.drawable.img2));
        list.add(new Photo(R.drawable.img3));
        list.add(new Photo(R.drawable.img4));

        return list;
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunable, 3000);
    }
}