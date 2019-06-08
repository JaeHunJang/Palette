package jjh.com.palette;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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


public class SaveThemeActivity extends AppCompatActivity {

    DBHelper dbHelper;
    Button[] save_tv_colorPalette = new Button[5];
    TextView[] save_tv_colorName = new TextView[5];
    EditText save_edt_themeName;
    Spinner save_sp_LibraryName;
    Spinner[] save_sp_Tags = new Spinner[3];
    Switch[] save_sw_ColorMode = new Switch[3];
    Button save_btn_reset, save_btn_save;
    String[] colors;
    StringChecker strChk;
    boolean flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savetheme);
        save_tv_colorPalette[0] = findViewById(R.id.save_btn_ColorPalette1);
        save_tv_colorPalette[1] = findViewById(R.id.save_btn_ColorPalette2);
        save_tv_colorPalette[2] = findViewById(R.id.save_btn_ColorPalette3);
        save_tv_colorPalette[3] = findViewById(R.id.save_btn_ColorPalette4);
        save_tv_colorPalette[4] = findViewById(R.id.save_btn_ColorPalette5);

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

        dbHelper = new DBHelper(this);
        strChk = new StringChecker();
        flag =false;
        colors = new String[5];

        Intent intent = getIntent();
        String[] temp = intent.getStringArrayExtra("colors");
        int j = 0;
        for (String t : temp) {
            if (t == null)
                continue;
            colors[j++] = t;
        }

        ArrayList[] result = dbHelper.select("Library","id = '"+Login.getInstance().getId() + "'");
        ArrayList<String> items = new ArrayList<>();
        for (ArrayList r : result){
            items.add(r.get(1).toString());
        }
        ArrayAdapter<String> spinnerAdpater = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, items);
        spinnerAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        save_sp_LibraryName.setAdapter(spinnerAdpater);


        colorUpdate(colors,colors);
        save_sw_ColorMode[0].setChecked(true);

        save_sw_ColorMode[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    save_sw_ColorMode[1].setChecked(false);
                    save_sw_ColorMode[2].setChecked(false);
                    colorUpdate(colors,colors);
                }
            }
        });
        save_sw_ColorMode[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    save_sw_ColorMode[0].setChecked(false);
                    save_sw_ColorMode[2].setChecked(false);
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
        save_sw_ColorMode[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    save_sw_ColorMode[0].setChecked(false);
                    save_sw_ColorMode[1].setChecked(false);
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

        save_edt_themeName.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (strChk.strPatternCheck(source)){ //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    flag = true;
                }
                else{
                    flag = false;
                }
                return source;
            }
        },new InputFilter.LengthFilter(20)});//최대 길이 20


        save_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    Toast.makeText(getApplicationContext(), "테마 이름은 영숫자만 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String themeName = save_edt_themeName.getText().toString();
                String lib = save_sp_LibraryName.getSelectedItem().toString();
                String color = "";
                String tag = "";
                Date current = new Date(); //오늘 날짜
                String today = String.format("%4d-%02d-%02d",current.getYear()+1900, current.getMonth()+1,current.getDate());
                for(String s : colors){
                    if (s == null)
                        continue;
                    color += s + "#";
                }
                for(Spinner s : save_sp_Tags){
                    if (s.getSelectedItem().toString().equals(" "))
                        continue;
                    tag += s.getSelectedItem().toString() + "#";
                }
                dbHelper.insert("Theme","'" + lib + "', '" + themeName + "', '" + color + "', '" + today + "', '" + tag + "'");
                finish();
            }
        });
    }
    void colorUpdate(String[] hex, String[] values){
        int j = 0;
        for (int i = 0; i < values.length; i++){
            if (values[i] != null && hex[i] != null) {
                save_tv_colorPalette[j].setBackgroundColor(Color.parseColor("#"+ hex[i]));
                save_tv_colorName[j++].setText(values[i]);
            }
        }
    }
}
