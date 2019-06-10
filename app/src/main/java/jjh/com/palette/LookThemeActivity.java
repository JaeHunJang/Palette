package jjh.com.palette;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LookThemeActivity extends AppCompatActivity {
    DBHelper dbHelper;
    Button[] look_tv_colorPalette = new Button[5];
    TextView[] look_tv_colorName = new TextView[5];
    TextView look_tv_userID,look_tv_themeName,look_tv_LibraryName,look_tv_Tags, look_tv_date;
    Switch[] look_sw_ColorMode = new Switch[3];
    Button look_btn_delete,look_btn_copy;
    String[] colors;
    StringChecker strChk;
    boolean flag;

    @Override
    protected void onCreate(@Nullable Bundle lookdInstanceState) {
        super.onCreate(lookdInstanceState);
        setContentView(R.layout.activity_looktheme);
        look_tv_colorPalette[0] = findViewById(R.id.look_btn_ColorPalette1);
        look_tv_colorPalette[1] = findViewById(R.id.look_btn_ColorPalette2);
        look_tv_colorPalette[2] = findViewById(R.id.look_btn_ColorPalette3);
        look_tv_colorPalette[3] = findViewById(R.id.look_btn_ColorPalette4);
        look_tv_colorPalette[4] = findViewById(R.id.look_btn_ColorPalette5);

        look_tv_colorName[0] = findViewById(R.id.look_tv_ColorName1);
        look_tv_colorName[1] = findViewById(R.id.look_tv_ColorName2);
        look_tv_colorName[2] = findViewById(R.id.look_tv_ColorName3);
        look_tv_colorName[3] = findViewById(R.id.look_tv_ColorName4);
        look_tv_colorName[4] = findViewById(R.id.look_tv_ColorName5);

        look_tv_userID = findViewById(R.id.look_tv_userId);
        look_tv_themeName = findViewById(R.id.look_tv_themeName);
        look_tv_LibraryName = findViewById(R.id.look_tv_libraryName);
        look_tv_Tags = findViewById(R.id.look_tv_tags);
        look_tv_date = findViewById(R.id.look_tv_date);

        look_sw_ColorMode[0] = findViewById(R.id.look_sw_hex);
        look_sw_ColorMode[1] = findViewById(R.id.look_sw_argb);
        look_sw_ColorMode[2] = findViewById(R.id.look_sw_cmyk);

        look_btn_delete = findViewById(R.id.look_btn_delete);
        look_btn_copy = findViewById(R.id.look_btn_copy);

        dbHelper = new DBHelper(this);
        strChk = new StringChecker();
        flag =false;
        colors = new String[5];

        Intent intent = getIntent();
        final String[] data = intent.getStringArrayExtra("selectedItem");
        String[] temp = data[1].split("#");
        int j = 0;
        for (String t : temp) {
            if (t == null)
                continue;
            colors[j++] = t;
        }
        String[] tags = data[3].split("#");

        String tag = "";
        for (int i = 0; i < tags.length; i++){
            if (!tags[i].equals("")) {
                tag += " #" + tags[i];
            }
        }
        String themeName = data[0];
        String date = data[2];
        String userId = data[4];
        String userLib = data[5];

        look_tv_themeName.setText(themeName);
        look_tv_date.setText(date);
        look_tv_userID.setText(userId);
        look_tv_LibraryName.setText(userLib);
        look_tv_Tags.setText(tag);

        if (userId.equals(Login.getInstance().getId())){ //본인이 만든거면 삭제만
            look_btn_copy.setVisibility(View.GONE);
        }
        else{ //본인 것이 아니면 복사 가능
            look_btn_delete.setVisibility(View.GONE);
        }

        colorUpdate(colors,colors);

        look_sw_ColorMode[0].setChecked(true);

        look_btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList[] result = dbHelper.select("Library","id = '"+Login.getInstance().getId()+"'");
                dbHelper.insert("Theme","'"+Login.getInstance().getId()+"','" + result[0].get(1).toString() + "', '" + data[0] + "', '" + data[1] + "', '" + data[2] + "', '" + data[3] + "'");
                Toast.makeText(getApplicationContext(),"복사되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LookThemeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        look_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.delete("Theme","id = '" + Login.getInstance().getId() + "' and library = '" + data[5] + "' and name = '" + data[0] + "'");
                Log.d("delete","id = '" + Login.getInstance().getId() + "' and library = '" + data[0] + "' and name = '" + data[5] + "'");
                Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LookThemeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        look_sw_ColorMode[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    look_sw_ColorMode[1].setChecked(false);
                    look_sw_ColorMode[2].setChecked(false);
                    colorUpdate(colors,colors);
                }
            }
        });
        look_sw_ColorMode[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    look_sw_ColorMode[0].setChecked(false);
                    look_sw_ColorMode[2].setChecked(false);
                    String[] values = new String[5];
                    int[] temp;
                    for (int i = 0; i < values.length; i++) {
                        if (colors[i] == null)
                            continue;
                        temp = ColorUtils.toColorARGB(Color.parseColor("#" + colors[i]));
                        values[i] = temp[0] + "," + temp[1] + "," + temp[2] + "," + temp[3];

                    }
                    colorUpdate(colors,values);
                }
            }
        });
        look_sw_ColorMode[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    look_sw_ColorMode[0].setChecked(false);
                    look_sw_ColorMode[1].setChecked(false);
                    String[] values = new String[5];
                    int[] temp, temp2;
                    temp2 = new int[4];
                    for (int i = 0; i < values.length; i++) {
                        if (colors[i] == null)
                            continue;
                        temp = ColorUtils.toColorARGB(Color.parseColor("#" + colors[i]));
                        ColorUtils.RGBToCMYK(temp,temp2);
                        values[i] = temp2[0] + "," + temp2[1] + "," + temp2[2] + "," + temp2[3] + ", " + Math.round((float)temp[0]/255.0f*100.0f) + "%";

                    }
                    colorUpdate(colors,values);
                }
            }
        });

    }
    void colorUpdate(String[] hex, String[] values){
        int j = 0;
        for (int i = 0; i < values.length; i++){
            if (values[i] != null && hex[i] != null) {
                look_tv_colorPalette[j].setBackgroundColor(Color.parseColor("#"+ hex[i]));
                look_tv_colorName[j++].setText(values[i]);
            }
        }
    }
}
