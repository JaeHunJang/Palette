package jjh.com.palette;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView main_bnv_menu;
    ViewPager viewPager;

    FragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        main_bnv_menu = findViewById(R.id.main_bnv_menu);
        main_bnv_menu.inflateMenu(R.menu.menu_main_bnv);


        viewPager = findViewById(R.id.main_container);
        adapter = new FragmentAdapter(getSupportFragmentManager());

        adapter.addItem(new UserInfoFragment());
        adapter.addItem(new MyLibraryFragment());
        adapter.addItem(new NewThemeFragment());
        adapter.addItem(new SearchFragment());
        adapter.notifyDataSetChanged();

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(2); //시작화면을 검색화면으로 띄어줌
        main_bnv_menu.setSelectedItemId(R.id.action_search);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                main_bnv_menu.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        main_bnv_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.action_acc:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_library:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_new:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.action_search:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });


    }
}
