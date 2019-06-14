package jjh.com.palette;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

//테마를 만드는 화면
public class NewThemeFragment extends Fragment {
    private ColorPickerView colorPickerView; //색상선택기
    private BrightnessSlideBar brightnessSlideBar; //채도 선택
    private AlphaSlideBar alphaSlideBar; //명도 선택
    private final Button[] new_btn_selectedColor = new Button[5]; //선택된 색상을 보여줄 뷰
    private String[] str_selectedColor = new String[5]; //선택된 색상값을 가진 배열
    private final int[] selectedRGB = new int[4]; //선택된 색상의 RGB 값
    private final int[] selectedCMYK = new int[4]; //선택된 색상의 CMYK 값
    private final SeekBar[] new_sb_rgb = new SeekBar[4]; //rgb 컨트롤
    private final EditText[] new_edt_rgb = new EditText[4];
    private final SeekBar[] new_sb_cmyk = new SeekBar[4]; //cmyk 컨트롤
    private final EditText[] new_edt_cmyk = new EditText[4];
    private final View[] mode = new ConstraintLayout[2]; //색상선택모드
    private int selectingColorNums; //현재 선택된 팔레트버튼
    Button new_btn_save, new_btn_next, new_btn_prev, new_btn_reset;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        /*************** 선언 및 초기화 ******************/
        View newTheme = inflater.inflate(R.layout.fragment_newtheme, container, false);
        final Spinner spinner_mode; //색상모드 선택

        selectingColorNums = 0; //색상 선택을 진행할 순서
        colorPickerView = newTheme.findViewById(R.id.colorPickerView);
        brightnessSlideBar = newTheme.findViewById(R.id.brightnessSlide);
        alphaSlideBar = newTheme.findViewById(R.id.alphaSlideBar);
        spinner_mode = newTheme.findViewById(R.id.new_sp_menu);
        mode[0] = newTheme.findViewById(R.id.container_rgb);
        mode[1] = newTheme.findViewById(R.id.container_cmyk);

        new_btn_selectedColor[0] = newTheme.findViewById(R.id.new_btn_color1);
        new_btn_selectedColor[1] = newTheme.findViewById(R.id.new_btn_color2);
        new_btn_selectedColor[2] = newTheme.findViewById(R.id.new_btn_color3);
        new_btn_selectedColor[3] = newTheme.findViewById(R.id.new_btn_color4);
        new_btn_selectedColor[4] = newTheme.findViewById(R.id.new_btn_color5);

        new_sb_rgb[0] = newTheme.findViewById(R.id.new_sb_rgb_alpha);
        new_sb_rgb[1] = newTheme.findViewById(R.id.new_sb_rgb_red);
        new_sb_rgb[2] = newTheme.findViewById(R.id.new_sb_rgb_green);
        new_sb_rgb[3] = newTheme.findViewById(R.id.new_sb_rgb_blue);

        new_edt_rgb[0] = newTheme.findViewById(R.id.new_edt_rgb_alpha);
        new_edt_rgb[1] = newTheme.findViewById(R.id.new_edt_rgb_red);
        new_edt_rgb[2] = newTheme.findViewById(R.id.new_edt_rgb_green);
        new_edt_rgb[3] = newTheme.findViewById(R.id.new_edt_rgb_blue);

        new_sb_cmyk[0] = newTheme.findViewById(R.id.new_sb_cmyk_cyan);
        new_sb_cmyk[1] = newTheme.findViewById(R.id.new_sb_cmyk_magenta);
        new_sb_cmyk[2] = newTheme.findViewById(R.id.new_sb_cmyk_yello);
        new_sb_cmyk[3] = newTheme.findViewById(R.id.new_sb_cmyk_key);

        new_edt_cmyk[0] = newTheme.findViewById(R.id.new_edt_cmyk_cyan);
        new_edt_cmyk[1] = newTheme.findViewById(R.id.new_edt_cmyk_magenta);
        new_edt_cmyk[2] = newTheme.findViewById(R.id.new_edt_cmyk_yello);
        new_edt_cmyk[3] = newTheme.findViewById(R.id.new_edt_cmyk_key);

        new_btn_save = newTheme.findViewById(R.id.new_btn_save);
        new_btn_next = newTheme.findViewById(R.id.new_btn_next);
        new_btn_prev = newTheme.findViewById(R.id.new_btn_prev);
        new_btn_reset = newTheme.findViewById(R.id.new_btn_reset);

        for (int i = 0; i < str_selectedColor.length; i++) //색상 저장 배열 초기화
            str_selectedColor[i] = null;

        for (int i = 0; i < selectedRGB.length; i++) {//선택색상 배열 초기화
            selectedRGB[i] = 255;
            selectedCMYK[i] = 0;
        }

        colorPickerView.attachBrightnessSlider(brightnessSlideBar);//색상선택기에 명도 투명도 선택기 추가
        colorPickerView.attachAlphaSlider(alphaSlideBar);

        //스피너에 색상 모드 리스트 적용
        ArrayAdapter<String> spinnerAdpater = new ArrayAdapter<>(newTheme.getContext(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.color_mode));
        spinnerAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mode.setAdapter(spinnerAdpater);
        spinner_mode.setSelection(0); //처음 선택은 argb

        Intent intent = getActivity().getIntent();
        boolean request = intent.getBooleanExtra("request", false); //수정버튼을 통해 fragment 가 Call 되었을때
        if (request) {
            final String[] data = intent.getStringArrayExtra("selectedItem"); //기존 데이터를 가져옴
            String[] temp = data[4].split("#"); //사용할 형태로 변환
            int j = 0;
            for (String t : temp) { //기존 색상을 적용
                if (t == null)
                    continue;
                str_selectedColor[j] = t;
                new_btn_selectedColor[j].setBackgroundColor(Color.parseColor("#" + str_selectedColor[j++]));
            }
            int[] colors = ColorUtils.toColorARGB(Color.parseColor("#" + str_selectedColor[0]));
            for (int i = 0; i < colors.length; i++) {
                selectedRGB[i] = colors[i];
            }
            colorPickerUpdate();
            changeEdtRgb();
            changeSbRgb();
            selectedColorHexUpdate();
        }
        new_btn_selectedColor[selectingColorNums].setText("Choose\nColor");

        /*************** 선언 및 초기화 ******************/
        spinner_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (View m : mode) { //색상모드들을 전부 보이지않게 초기화
                    m.setVisibility(View.INVISIBLE);
                }
                mode[position].setVisibility(View.VISIBLE); //선택한 모드만 보이게 설정
                int[] tempColor = colorPickerView.getColorEnvelope().getArgb(); //선택된 색상을 가져옴
                for (int i = 0; i < tempColor.length; i++) { //색상정보를 argb 값에 저장
                    selectedRGB[i] = tempColor[i];
                }
                changeAll(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new_btn_save.setOnClickListener(new View.OnClickListener() { //저장버튼
            @Override
            public void onClick(View v) {
                Intent outIntent = new Intent(getContext(), SaveThemeActivity.class);
                Intent intent = getActivity().getIntent();
                boolean request = intent.getBooleanExtra("request", false);
                if (request) {
                    final String[] data = intent.getStringArrayExtra("selectedItem");
                    outIntent.putExtra("selectedItem", data);
                    outIntent.putExtra("request", true);
                }
                outIntent.putExtra("colors", str_selectedColor);
                startActivity(outIntent);
            }
        });
        new_btn_prev.setOnClickListener(new View.OnClickListener() { //이전버튼
            @Override
            public void onClick(View v) {
                if (selectingColorNums > 0) {
                    selectingColorNums--;
                    if (str_selectedColor[selectingColorNums] == null) {
                        for (int i = 0; i < selectedRGB.length; i++) //선택색상 배열 초기화
                            selectedRGB[i] = 0;
                    } else {
                        int[] colors = ColorUtils.toColorARGB(Color.parseColor("#" + str_selectedColor[selectingColorNums]));
                        for (int i = 0; i < colors.length; i++) {
                            selectedRGB[i] = colors[i];
                        }
                        colorPickerUpdate();
                        changeAll(spinner_mode.getSelectedItemPosition());
                    }
                    selectedColorHexUpdate();
                    new_btn_selectedColor[selectingColorNums].setText("Choose\nColor");
                }
            }
        });
        new_btn_next.setOnClickListener(new View.OnClickListener() { //다음버튼
            @Override
            public void onClick(View v) {
                if (selectingColorNums < new_btn_selectedColor.length - 1) {
                    selectingColorNums++;
                    if (str_selectedColor[selectingColorNums] == null) {
                        for (int i = 0; i < selectedRGB.length; i++) //선택색상 배열 초기화
                            selectedRGB[i] = 0;
                    } else {
                        int[] colors = ColorUtils.toColorARGB(Color.parseColor("#" + str_selectedColor[selectingColorNums]));
                        for (int i = 0; i < colors.length; i++) {
                            selectedRGB[i] = colors[i];
                        }
                        colorPickerUpdate();
                        changeAll(spinner_mode.getSelectedItemPosition());
                    }
                    selectedColorHexUpdate();
                    new_btn_selectedColor[selectingColorNums].setText("Choose\nColor");
                }
            }
        });
        new_btn_reset.setOnClickListener(new View.OnClickListener() { //색상취소버튼
            @Override
            public void onClick(View v) {
                str_selectedColor[selectingColorNums] = null;
                selectedColorHexUpdate();
                new_btn_selectedColor[selectingColorNums].setBackgroundColor(Color.parseColor("#00FFFFFF")); //흰색으로 만들어서 초기화
                new_btn_selectedColor[selectingColorNums].setText("Choose\nColor");

            }
        });
        for (int i = 0; i < new_btn_selectedColor.length; i++) {
            final int index = i;
            new_btn_selectedColor[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectingColorNums = index; //버튼을 누르면 해당 버튼의 색상선택으로 변경
                    if (str_selectedColor[selectingColorNums] != null) {
                        int[] colors = ColorUtils.toColorARGB(Color.parseColor("#" + str_selectedColor[index]));
                        for (int i = 0; i < colors.length; i++) {
                            selectedRGB[i] = colors[i];
                        }
                        colorPickerUpdate();
                        changeAll(spinner_mode.getSelectedItemPosition());
                    }
                    selectedColorHexUpdate();
                    new_btn_selectedColor[selectingColorNums].setText("Choose\nColor");
                }
            });
        }

        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                if (fromUser) {
                    int[] tempColor = envelope.getArgb(); //선택된 색상을 가져옴
                    for (int i = 0; i < tempColor.length; i++) {
                        selectedRGB[i] = tempColor[i];
                    }
                    changeAll(spinner_mode.getSelectedItemPosition());
                }
                str_selectedColor[selectingColorNums] = ColorUtils.toHexCode(Color.argb(selectedRGB[0], selectedRGB[1], selectedRGB[2], selectedRGB[3])); //색상정보를 16진수 코드로 저장
                new_btn_selectedColor[selectingColorNums].setBackgroundColor(Color.parseColor("#" + str_selectedColor[selectingColorNums])); //선택된 색상으로 선택팔레트(버튼)에 색상 적용
                selectedColorHexUpdate();
            }
        });


        for (int i = 0; i < new_sb_rgb.length; i++) {
            final int index = i;
            /***** ARGB 이벤트 설정 *****/
            new_sb_rgb[index].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) { //SeekBar 값이 바뀌면 EditText 의 값도 바꿔주고 색상선택기 리로드
                        selectedRGB[index] = progress;
                        changeEdtRgb();
                        colorPickerUpdate();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            new_edt_rgb[index].addTextChangedListener(new TextWatcher() { //EditText 값이 바뀌면 SeekBar 의 값도 바꿔줌
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        int temp = Integer.parseInt(s.toString());
                        selectedRGB[index] = temp;
                    }
                }
            });
            new_edt_rgb[index].setFilters(new InputFilter[]{new MinMaxFilter(0, 255), new InputFilter.LengthFilter(3)}); //최소,최대값,글자수 필터 적용
            new_edt_rgb[index].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) { //값 입력후 Enter 입력시 색상선택기 리로드
                        changeSbRgb();
                        colorPickerUpdate();
                        return true;
                    }
                    return false;
                }
            });
            /***** ARGB 이벤트 설정 *****/

            /***** CMYK 이벤트 설정 *****/
            new_sb_cmyk[index].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) { //SeekBar 값이 바뀌면 EditText 의 값도 바꿔주고 색상선택기 리로드
                        selectedCMYK[index] = progress;
                        changeEdtCmyk();
                        ColorUtils.CMYKToRGB(selectedCMYK, selectedRGB);
                        colorPickerUpdate();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            new_edt_cmyk[index].addTextChangedListener(new TextWatcher() { //EditText 값이 바뀌면 SeekBar 의 값도 바꿔줌
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        int temp = Integer.parseInt(s.toString());
                        selectedCMYK[index] = temp;
                    }
                }
            });
            new_edt_cmyk[index].setFilters(new InputFilter[]{new MinMaxFilter(0, 100), new InputFilter.LengthFilter(3)}); //최소,최대값,글자수 필터 적용
            new_edt_cmyk[index].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) { //값 입력후 Enter 입력시 색상선택기 리로드
                        changeSbCmyk();
                        ColorUtils.CMYKToRGB(selectedCMYK, selectedRGB);
                        colorPickerUpdate();
                        return true;
                    }
                    return false;
                }
            });
            /***** CMYK 이벤트 설정 *****/
        }
        return newTheme;
    }

    void colorPickerUpdate() {  //colorPickerView 업데이트
        float[] hsv = new float[3];
        int selectedColor = Color.argb(selectedRGB[0], selectedRGB[1], selectedRGB[2], selectedRGB[3]);
        Color.colorToHSV(selectedColor, hsv); //색상값을 hsv 모델에 맞게 변경
        int alphaX = Math.round(((float) selectedRGB[0] / 255.0f) * (float) alphaSlideBar.getMeasuredWidth()); //투명도를 alphaSlideBar 가로길이로 계산
        int brightX = Math.round(hsv[2] * (float) brightnessSlideBar.getMeasuredWidth()); //채도를 brightnesSlideBar 가로길이로 계산
        alphaSlideBar.updateSelectorX(alphaX); //selector 위치 전부 변경해줌
        brightnessSlideBar.updateSelectorX(brightX);
        colorPickerView.selectByHsv(Color.HSVToColor(hsv));
    }

    void changeAll(int position) {
        switch (position) { //선택된 모드
            case 0:
                changeEdtRgb(); //현재 선택된 색상정보로 EditText, SeekBar 값 변경
                changeSbRgb();
                break;
            case 1:
                ColorUtils.RGBToCMYK(selectedRGB, selectedCMYK); //ARGB 기준으로 색상선택이 되기 때문에 CMYK 로 변환후 CMYK 를 적용
                changeEdtCmyk();
                changeSbCmyk();
                break;
        }
    }

    void changeEdtRgb() { //ARGB SeekBar 값이 변경됬을때 EditText 값 업데이트
        for (int i = 0; i < new_edt_rgb.length; i++) {
            String temp = selectedRGB[i] + "";
            new_edt_rgb[i].setText(temp);
        }
    }

    void changeSbRgb() { //ARGB EditText 값이 변경됬을때 SeekBar 값 업데이트
        for (int i = 0; i < new_sb_rgb.length; i++) {
            new_sb_rgb[i].setProgress(selectedRGB[i]);
        }
    }

    void changeEdtCmyk() { //CMYK EditText 값이 변경됬을때 SeekBar 값 업데이트
        for (int i = 0; i < new_edt_cmyk.length; i++) {
            String temp = selectedCMYK[i] + "";
            new_edt_cmyk[i].setText(temp);
        }
    }

    void changeSbCmyk() { //CMYK EditText 값이 변경됬을때 SeekBar 값 업데이트
        for (int i = 0; i < new_sb_cmyk.length; i++) {
            new_sb_cmyk[i].setProgress(selectedCMYK[i]);
        }
    }

    void selectedColorHexUpdate() { //색상선택 팔레트의 색상값 지정
        for (int i = 0; i < new_btn_selectedColor.length; i++) {
            if (str_selectedColor[i] == null)
                new_btn_selectedColor[i].setText("");
            else
                new_btn_selectedColor[i].setText(str_selectedColor[i]);
        }
    }
}
