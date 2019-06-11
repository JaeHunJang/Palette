package jjh.com.palette;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
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
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//테마를 저장하는 화면
public class SaveThemeActivity extends AppCompatActivity {
    DBHelper dbHelper;
    Button[] save_tv_colorPalette = new Button[5]; //색상을 보여줄 뷰
    TextView[] save_tv_colorName = new TextView[5]; //색상코드를 보여줄 뷰
    EditText save_edt_themeName; //입력받을 테마이름
    Spinner save_sp_LibraryName; //선택된 라이브러리
    Spinner[] save_sp_Tags = new Spinner[3]; //선택할 태그들
    Switch[] save_sw_ColorMode = new Switch[3]; //확인해볼 색상 값
    Button save_btn_reset, save_btn_save; //저장, 취소버튼
    String[] colors; //색상코드를 가진 배열
    StringChecker strChk; //문자열 규칙을 확인할 객체
    boolean flag; //입력받은 값이 정상인지 확인할 플래그

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savetheme);
        /*************** 선언 및 초기화 ******************/
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
        save_sp_LibraryName = findViewById(R.id.save_sp_libraryName);

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
        flag = false;
        colors = new String[5];

        Intent intent = getIntent();
        String[] temp;
        temp = intent.getStringArrayExtra("colors"); //받아온 색상정보를 저장
        int j = 0;
        for (String t : temp) {
            if (t == null)
                continue;
            colors[j++] = t;
        }
        try {
            ArrayList[] result = dbHelper.select("Library", "id = '" + Login.getInstance().getId() + "'"); //로그인한 아이디의 라이브러리를 불러옴
            ArrayList<String> items = new ArrayList<>();
            for (ArrayList r : result) {
                items.add(r.get(1).toString());
            }
            ArrayAdapter<String> spinnerAdpater = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, items);
            spinnerAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            save_sp_LibraryName.setAdapter(spinnerAdpater);
            save_sp_LibraryName.setSelection(items.size() - 1);
        } catch (SQLException sqle) {
            dbHelper.getError(sqle);
        }

        colorUpdate(colors, colors); //변경된 색상 모드에 맞게 색상코드값을 바꿔주는 메소드
        save_sw_ColorMode[0].setChecked(true); //처음 선택은 HEX code
        /*************** 선언 및 초기화 ******************/
        save_sw_ColorMode[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //색상정보 HEX code
                if (isChecked) {
                    save_sw_ColorMode[1].setChecked(false);
                    save_sw_ColorMode[2].setChecked(false);
                    colorUpdate(colors, colors);
                }
            }
        });
        save_sw_ColorMode[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //색상정보 ARGB
                if (isChecked) {
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
                    colorUpdate(colors, values);
                }
            }
        });
        save_sw_ColorMode[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //색상정보 CMYK
                if (isChecked) {
                    save_sw_ColorMode[0].setChecked(false);
                    save_sw_ColorMode[1].setChecked(false);
                    String[] values = new String[5];
                    int[] temp, temp2;
                    temp2 = new int[4];
                    for (int i = 0; i < values.length; i++) {
                        if (colors[i] == null)
                            continue;
                        temp = ColorUtils.toColorARGB(Color.parseColor("#" + colors[i]));
                        ColorUtils.RGBToCMYK(temp, temp2);
                        values[i] = temp2[0] + "," + temp2[1] + "," + temp2[2] + "," + temp2[3] + ", " + Math.round((float) temp[0] / 255.0f * 100.0f) + "%";

                    }
                    colorUpdate(colors, values);
                }
            }
        });

        save_edt_themeName.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) { //이름 규칙이 맞는지 확인
                if (strChk.strPatternCheck(source)) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    flag = true;
                } else {
                    flag = false;
                }
                return source;
            }
        }, new InputFilter.LengthFilter(20)});//최대 길이 20

        //취소 버튼을 누르면 전 화면으로 돌아감
        save_btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //저장버튼을 눌렀을 때 이벤트
        save_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) { //입력 정보가 틀렸을 때 메시지 출력
                    Toast.makeText(getApplicationContext(), "테마 이름은 영숫자만 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String themeName = save_edt_themeName.getText().toString();
                String lib = save_sp_LibraryName.getSelectedItem().toString();
                String color = "";
                String tag = "";
                Date current = new Date(); //오늘 날짜
                String today = String.format("%4d-%02d-%02d", current.getYear() + 1900, current.getMonth() + 1, current.getDate());
                for (String s : colors) {
                    if (s == null)
                        continue;
                    color += s + "#";
                }
                for (Spinner s : save_sp_Tags) { //색상정보를 DB에 넣게 가공
                    if (s.getSelectedItem().toString().equals(" "))
                        continue;
                    tag += s.getSelectedItem().toString() + "#";
                }
                try {
                    dbHelper.insert("Theme", "'" + Login.getInstance().getId() + "','" + lib + "', '" + themeName + "', '" + color + "', '" + today + "', '" + tag + "'");
                } catch (SQLException sqle) {
                    dbHelper.getError(sqle);
                }
                Intent intent = new Intent(SaveThemeActivity.this, MainActivity.class); //저장이 되면 메인화면으로 돌아감
                startActivity(intent);
                finish();
            }
        });
    }

    void colorUpdate(String[] hex, String[] values) {//변경된 색상 모드에 맞게 색상코드값을 바꿔주는 메소드
        int j = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null && hex[i] != null) {
                save_tv_colorPalette[j].setBackgroundColor(Color.parseColor("#" + hex[i]));
                save_tv_colorName[j++].setText(values[i]);
            }
        }
    }
}
