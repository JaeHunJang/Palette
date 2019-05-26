package jjh.com.palette;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView main_bnv_menu;

        main_bnv_menu = findViewById(R.id.main_bnv_menu);
        main_bnv_menu.inflateMenu(R.menu.menu_main_bnv);

        /*
        findViewById(R.id.main_fab_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */
        main_bnv_menu.setSelected(false);
        main_bnv_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id){
                    case R.id.action_search:
                         intent = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_library:
                        intent = new Intent(MainActivity.this, MyLibraryActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_new:
                        intent = new Intent(MainActivity.this, NewThemeActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });


    }
}
