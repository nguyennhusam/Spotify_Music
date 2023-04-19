package hcmute.edu.vn.musicmediaplayer.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerDiaNhac extends FragmentPagerAdapter {

    public final ArrayList<Fragment> fragments = new ArrayList<>();

    public ViewPagerDiaNhac(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void AddFragment(Fragment fragment) {
        fragments.add(fragment);
    }

}

