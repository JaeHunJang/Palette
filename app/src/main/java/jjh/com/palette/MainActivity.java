package jjh.com.palette;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

//https://stackoverflow.com/questions/47714606/viewpager-using-bottom-navigation-view-is-not-swiping-the-fragments
//BottomNavigationView 사용법 인용
public class MainActivity extends AppCompatActivity {
    BottomNavigationView main_bnv_menu;
    ViewPager viewPager;
    FragmentAdapter fragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final int[] pages = {R.id.action_acc, R.id.action_library, R.id.action_new, R.id.action_search}; //띄어줄 화면 메뉴(바텀네비게이션뷰) 목록

        main_bnv_menu = findViewById(R.id.main_bnv_menu);
        main_bnv_menu.inflateMenu(R.menu.menu_main_bnv);

        viewPager = findViewById(R.id.main_container);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        fragmentAdapter.addItem(new UserInfoFragment());
        fragmentAdapter.addItem(new LibraryFragment());
        fragmentAdapter.addItem(new NewThemeFragment());
        fragmentAdapter.addItem(new SearchFragment());
        fragmentAdapter.notifyDataSetChanged();

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(3); //시작화면을 검색화면으로 띄어줌
        main_bnv_menu.setSelectedItemId(pages[3]);
        if (!Login.getInstance().getLoginState()){ //로그인이 되어있지 않으면 어플리케이션 종료
            finish();
        }
        Intent intent = getIntent();
        int page = intent.getIntExtra("page",-1); //시작할 화면 선택
        if (page != -1){ //값이 있으면 해당 화면으로 이동
            viewPager.setCurrentItem(page);
            main_bnv_menu.setSelectedItemId(pages[page]);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < 4)
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
