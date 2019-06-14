package jjh.com.palette;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//테마 정보를 보는 화면
public class LookThemeActivity extends AppCompatActivity {
    DBHelper dbHelper;
    Button[] look_tv_colorPalette = new Button[5]; //색상을 보여줄 뷰
    TextView[] look_tv_colorName = new TextView[5]; //색상코드를 보여줄 뷰
    TextView look_tv_userID, look_tv_themeName, look_tv_LibraryName, look_tv_Tags, look_tv_date; //테마 이름, 만든이, 만든날짜 등의 정보를 보여줄 뷰
    Switch[] look_sw_ColorMode = new Switch[3]; //색상정보를 다른것으로 보여줄 뷰
    Button look_btn_delete, look_btn_copy, look_btn_update; //색상테마 삭제, 복사 버튼
    String[] colors; //색상정보를 가질 배열
    StringChecker strChk; //문자열 규칙을 확인할 객체
    String[] items; //복사할때 복사될 라이브러리들을 가진 배열
    int selected; //선택된 라이브러리

    @Override
    protected void onCreate(@Nullable Bundle lookdInstanceState) {
        super.onCreate(lookdInstanceState);
        setContentView(R.layout.activity_looktheme);
        /*************** 선언 및 초기화 ******************/
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
        look_btn_update = findViewById(R.id.look_btn_update);
        look_btn_copy = findViewById(R.id.look_btn_copy);

        dbHelper = new DBHelper(this);
        strChk = new StringChecker();
        colors = new String[5];

        Intent intent = getIntent();
        final String[] data = intent.getStringArrayExtra("selectedItem"); //전달받은 테마의 정보

        String userId = data[1];
        String userLib = data[2];
        String themeName = data[3];
        String date = data[5];

        String[] temp = data[4].split("#"); //사용할 형태로 변환
        int j = 0;
        for (String t : temp) {
            if (t == null)
                continue;
            colors[j++] = t;
        }

        String[] tags = data[6].split("#"); //사용할 형태로 변환
        String tag = "";
        for (int i = 0; i < tags.length; i++) {
            if (!tags[i].equals("")) {
                tag += " #" + tags[i];
            }
        }


        look_tv_themeName.setText(themeName);//뷰에 데이터 출력
        look_tv_date.setText(date);
        look_tv_userID.setText(userId);
        look_tv_LibraryName.setText(userLib);
        look_tv_Tags.setText(tag);

        if (userId.equals(Login.getInstance().getId())) { //본인이 만든거면 복사버튼 비활성화
            look_btn_copy.setVisibility(View.GONE);
        } else { //본인이 만든것이 아니면 복사버튼만 활성화
            look_btn_delete.setVisibility(View.GONE);
            look_btn_update.setVisibility(View.GONE);
        }
        colorUpdate(colors, colors);
        look_sw_ColorMode[0].setChecked(true);
        /*************** 선언 및 초기화 ******************/
        //수정버튼 이벤트
        look_btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LookThemeActivity.this, MainActivity.class);
                intent.putExtra("selectedItem", data);
                intent.putExtra("page", 2);//테마생성화면으로 이동
                intent.putExtra("request", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //이전에 실행된 메인 화면을 종료함
                startActivity(intent);
                finish();
            }
        });
        //복사버튼 이벤트
        look_btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Vector[] result = dbHelper.select("Library", "id = '" + Login.getInstance().getId() + "'"); //로그인된 아이디의 라이브러리를 가져옴
                    items = new String[result.length];
                    for (int i = 0; i < result.length; i++) {
                        items[i] = result[i].get(1).toString();
                    }
                    selected = items.length - 1;
                    AlertDialog.Builder dlg = new AlertDialog.Builder(LookThemeActivity.this); //라이브러리 선택 대화상자 출력
                    dlg.setTitle("라이브러리 선택")
                            .setSingleChoiceItems(items, items.length - 1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    selected = which;
                                }
                            })
                            .setNegativeButton("닫기", null)
                            .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbHelper.insert("Theme(id,library,name,color,date,tags)", "'" + Login.getInstance().getId() + "','" + items[selected] + "', '" + data[3] + "', '" + data[4] + "', '" + data[5] + "', '" + data[6] + "'");
                                    Toast.makeText(getApplicationContext(), "복사되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LookThemeActivity.this, MainActivity.class);
                                    intent.putExtra("page",1);//라이브러리화면으로 이동
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //이전에 실행된 메인 화면을 종료함
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .show();

                } catch (SQLException sqle) {
                    dbHelper.getError(sqle);
                }
            }
        });
        //삭제버튼 이벤트
        look_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbHelper.delete("Theme", "num = " + data[0] + " and id = '" + Login.getInstance().getId() + "' and library = '" + data[2] + "' and name = '" + data[3] + "'");
                    Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LookThemeActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //이전에 실행된 메인 화면을 종료함
                    intent.putExtra("page",1); //라이브러리화면으로 이동
                    startActivity(intent);
                    finish();
                } catch (SQLException sqle) {
                    dbHelper.getError(sqle);
                }
            }
        });
        //색상모드 HEX Code
        look_sw_ColorMode[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    look_sw_ColorMode[1].setChecked(false);
                    look_sw_ColorMode[2].setChecked(false);
                    colorUpdate(colors, colors);
                }
            }
        });
        //색상모드 ARGB
        look_sw_ColorMode[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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
                    colorUpdate(colors, values);
                }
            }
        });
        //색상모드 CMYK
        look_sw_ColorMode[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    look_sw_ColorMode[0].setChecked(false);
                    look_sw_ColorMode[1].setChecked(false);
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

    }

    //변경된 색상모드에 맞게 글자를 변경
    void colorUpdate(String[] hex, String[] values) {
        int j = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null && hex[i] != null) {
                look_tv_colorPalette[j].setBackgroundColor(Color.parseColor("#" + hex[i]));
                look_tv_colorName[j++].setText(values[i]);
            }
        }
    }
}
