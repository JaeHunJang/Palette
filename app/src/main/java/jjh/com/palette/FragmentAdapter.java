package jjh.com.palette;

import android.view.ViewGroup;

import java.util.Vector;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//https://stackoverflow.com/questions/47714606/viewpager-using-bottom-navigation-view-is-not-swiping-the-fragments
//BottomNavigationView 사용법 인용
public class FragmentAdapter extends FragmentPagerAdapter {
    private Vector<Fragment> fragments = new Vector<>();

    FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //super.destroyItem(container, position, object);
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

