package jjh.com.palette;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SaveThemeActivity extends AppCompatActivity {

    TextView[] save_tv_colorPalette = new TextView[5];
    TextView[] save_tv_colorName = new TextView[5];
    EditText save_edt_themeName;
    Spinner save_sp_LibraryName;
    Spinner[] save_sp_Tags = new Spinner[3];
    Switch[] save_sw_ColorMode = new Switch[3];
    Button save_btn_reset, save_btn_save;
    String[] colors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savetheme);
        Log.d("도착","도착");
        save_tv_colorPalette[0] = findViewById(R.id.save_tv_ColorPalette1);
        save_tv_colorPalette[1] = findViewById(R.id.save_tv_ColorPalette2);
        save_tv_colorPalette[2] = findViewById(R.id.save_tv_ColorPalette3);
        save_tv_colorPalette[3] = findViewById(R.id.save_tv_ColorPalette4);
        save_tv_colorPalette[4] = findViewById(R.id.save_tv_ColorPalette5);

        save_tv_colorName[0] = findViewById(R.id.save_tv_ColorName1);
        save_tv_colorName[1] = findViewById(R.id.save_tv_ColorName2);
        save_tv_colorName[2] = findViewById(R.id.save_tv_ColorName3);
        save_tv_colorName[3] = findViewById(R.id.save_tv_ColorName4);
        save_tv_colorName[4] = findViewById(R.id.save_tv_ColorName5);

        save_edt_themeName = findViewById(R.id.save_edt_themeName);
        save_sp_LibraryName = findViewById(R.id.save_sp_library);

        save_sp_Tags[0] = findViewById(R.id.save_sp_tag1);
        save_sp_Tags[1] = findViewById(R.id.save_sp_tag2);
        save_sp_Tags[2] = findViewById(R.id.save_sp_tag3);

        save_sw_ColorMode[0] = findViewById(R.id.save_sw_hex);
        save_sw_ColorMode[1] = findViewById(R.id.save_sw_argb);
        save_sw_ColorMode[2] = findViewById(R.id.save_sw_cmyk);

        save_btn_reset = findViewById(R.id.save_btn_reset);
        save_btn_save = findViewById(R.id.save_btn_save);

        Intent intent = getIntent();
        colors = intent.getStringArrayExtra("colors");
        Log.d("색깔배열", Arrays.toString(colors));


        for (int i = 0; i <colors.length; i++){
            if (colors[i] != null) {
                save_tv_colorPalette[i].setBackgroundColor(Color.parseColor("#"+colors[i]));
                save_tv_colorName[i].setText(colors[i]);
            }
//            Log.d("색",Color.parseColor(colors[i])+"");
        }
    }
}
