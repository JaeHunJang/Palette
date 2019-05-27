package jjh.com.palette;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();

    FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override //표시할 fragment
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    //List 크기 반환
    @Override
    public int getCount() {
        return fragments.size();
    }

    //List 에 Fragment 추가
    void addItem(Fragment fragment){
        fragments.add(fragment);
    }
}

