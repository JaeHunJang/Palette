package jjh.com.palette;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;


public class NewThemeFragment extends Fragment {
    ColorPickerView colorPickerView; //색상선택기
    BrightnessSlideBar brightnessSlideBar; //채도 선택
    AlphaSlideBar alphaSlideBar; //명도 선택
    final Button[] btnColor = new Button[5]; //선택된 색상을 보여줄 뷰
    final String[] colorStr = new String[5]; //선택된 색상값을 가진 배열
    final int[] selectedRGB = new int[4]; //선택된 색상의 RGB 값
    final int[] selectedCMYK = new int[4]; //선택된 색상의 CMYK 값
    final SeekBar[] sbRgb = new SeekBar[4]; //rgb 컨트롤
    final EditText[] edtRgb = new EditText[4];
    final SeekBar[] sbCmyk = new SeekBar[4]; //cmyk 컨트롤
    final EditText[] edtCmyk = new EditText[4];
    final View[] mode = new ConstraintLayout[2];
    int selectingColorNums;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View newTheme = inflater.inflate(R.layout.fragment_newtheme,container,false);

        final Spinner spinner_mode; //색상모드 선택

        selectingColorNums = 0; //색상 선택을 진행할 순서

        colorPickerView = newTheme.findViewById(R.id.colorPickerView);
        brightnessSlideBar = newTheme.findViewById(R.id.brightnessSlide);
        alphaSlideBar = newTheme.findViewById(R.id.alphaSlideBar);
        spinner_mode = newTheme.findViewById(R.id.new_sp_menu);
        mode[0] = newTheme.findViewById(R.id.container_rgb);
        mode[1] = newTheme.findViewById(R.id.container_cmyk);

        btnColor[0] = newTheme.findViewById(R.id.new_btn_color1);
        btnColor[1] = newTheme.findViewById(R.id.new_btn_color2);
        btnColor[2] = newTheme.findViewById(R.id.new_btn_color3);
        btnColor[3] = newTheme.findViewById(R.id.new_btn_color4);
        btnColor[4] = newTheme.findViewById(R.id.new_btn_color5);

        sbRgb[0] = newTheme.findViewById(R.id.new_sb_rgb_alpha);
        sbRgb[1] = newTheme.findViewById(R.id.new_sb_rgb_red);
        sbRgb[2] = newTheme.findViewById(R.id.new_sb_rgb_green);
        sbRgb[3] = newTheme.findViewById(R.id.new_sb_rgb_blue);

        edtRgb[0] = newTheme.findViewById(R.id.new_edt_rgb_alpha);
        edtRgb[1] = newTheme.findViewById(R.id.new_edt_rgb_red);
        edtRgb[2] = newTheme.findViewById(R.id.new_edt_rgb_green);
        edtRgb[3] = newTheme.findViewById(R.id.new_edt_rgb_blue);

        sbCmyk[0] = newTheme.findViewById(R.id.new_sb_cmyk_cyan);
        sbCmyk[1] = newTheme.findViewById(R.id.new_sb_cmyk_magenta);
        sbCmyk[2] = newTheme.findViewById(R.id.new_sb_cmyk_yello);
        sbCmyk[3] = newTheme.findViewById(R.id.new_sb_cmyk_key);

        edtCmyk[0] = newTheme.findViewById(R.id.new_edt_cmyk_cyan);
        edtCmyk[1] = newTheme.findViewById(R.id.new_edt_cmyk_magenta);
        edtCmyk[2] = newTheme.findViewById(R.id.new_edt_cmyk_yello);
        edtCmyk[3] = newTheme.findViewById(R.id.new_edt_cmyk_key);

        for (int i = 0; i < colorStr.length; i++) //색상 저장 배열 초기화
            colorStr[i] = null;

        for (int i = 0; i < selectedRGB.length; i++) {//선택색상 배열 초기화
            selectedRGB[i] = 255;
            selectedCMYK[i] = 0;
        }


        colorPickerView.attachBrightnessSlider(brightnessSlideBar);//색상선택기에 명도 투명도 선택기 추가
        colorPickerView.attachAlphaSlider(alphaSlideBar);

        selectionButton();//처음 선택버튼 알림
        changeColor();

        //스피너에 색상 모드 리스트 적용
        ArrayAdapter<String> spinnerAdpater = new ArrayAdapter<>(newTheme.getContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.color_mode));
        spinnerAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mode.setAdapter(spinnerAdpater);
        spinner_mode.setSelection(1); //처음 선택은 argb
        spinner_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (View m : mode){
                    m.setVisibility(View.INVISIBLE);
                }
                mode[position].setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        newTheme.findViewById(R.id.new_btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        newTheme.findViewById(R.id.new_btn_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectingColorNums > 0) {
                    for (int i = 0; i < selectedRGB.length; i++) //선택색상 배열 초기화
                        selectedRGB[i] = 0;
                    selectingColorNums--;
                    selectionButton();
                }
            }
        });
        newTheme.findViewById(R.id.new_btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectingColorNums < btnColor.length-1) {
                    for (int i = 0; i < selectedRGB.length; i++) //선택색상 배열 초기화
                        selectedRGB[i] = 0;
                    selectingColorNums++;
                    selectionButton();
                }
            }
        });
        for (int i = 0; i < btnColor.length; i++) {
            final int index = i;
            btnColor[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectingColorNums = index; //버튼을 누르면 해당 버튼의 색상선택으로 변경
                    selectionButton();
                }
            });
        }

        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                int position = spinner_mode.getSelectedItemPosition();
                int[] tempColor = envelope.getArgb(); //선택된 색상을 가져옴
                for (int i = 0; i < tempColor.length; i++) { //색상정보를 SeekBar, EditText 에 적용
                    selectedRGB[i] = tempColor[i];
                }
                RGBToCMYK(selectedRGB);
                changeEdtRgb();
                changeSbRgb();
                changeEdtCmyk();
                changeSbCmyk();
                /*if (fromUser){
                    switch (position){
                        case 0:
                            changeEdtRgb();
                            changeSbRgb();
                            changeEdtCmyk();
                            changeSbCmyk();
                            Log.d("user0", Arrays.toString(selectedRGB)+" / "+Arrays.toString(selectedCMYK));

                            break;
                        case 1:
                            changeEdtCmyk();
                            changeSbCmyk();
                            changeEdtRgb();
                            changeSbRgb();Log.d("user1", Arrays.toString(selectedRGB)+" / "+Arrays.toString(selectedCMYK));
                            break;
                    }
                }
                else
                {
                    switch (position){
                        case 0:
                            changeEdtRgb();
                            changeSbRgb();
                            changeEdtCmyk();
                            changeSbCmyk();Log.d("userNon0", Arrays.toString(selectedRGB)+" / "+Arrays.toString(selectedCMYK));
                            break;
                        case 1:
                            changeEdtCmyk();
                            changeSbCmyk();
                            changeEdtRgb();
                            changeSbRgb();Log.d("userNon1", Arrays.toString(selectedRGB)+" / "+Arrays.toString(selectedCMYK));
                            break;
                    }
                }*/
                Log.d("rgbcomyk11", Arrays.toString(selectedRGB)+" / "+Arrays.toString(selectedCMYK));

                btnColor[selectingColorNums].setBackgroundColor(envelope.getColor());
                colorStr[selectingColorNums] = envelope.getHexCode();
                selectionButton();

            }
        });


        for (int i = 0; i < sbRgb.length; i++) {
            final int index = i;
            /***** ARGB 이벤트 설정 *****/
            sbRgb[index].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) { //SeekBar 값이 바뀌면 EditText 의 값도 바꿔주고 색상선택기 리로드
                        selectedRGB[index] = progress;
                        changeEdtRgb();
                        RGBToCMYK(selectedRGB);
                        changeEdtCmyk();
                        changeSbCmyk();
                        changeColor();
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            edtRgb[index].addTextChangedListener(new TextWatcher() { //EditText 값이 바뀌면 SeekBar 의 값도 바꿔줌
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
            edtRgb[index].setFilters(new InputFilter[]{new MinMaxFilter(0,255),new InputFilter.LengthFilter(3)}); //최소,최대값,글자수 필터 적용
            edtRgb[index].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) { //값 입력후 Enter 입력시 색상선택기 리로드
                        changeSbRgb();
                        RGBToCMYK(selectedRGB);
                        changeEdtCmyk();
                        changeSbCmyk();
                        changeColor();
                        return true;
                    }
                    return false;
            }});
            /***** ARGB 이벤트 설정 *****/

            /***** CMYK 이벤트 설정 *****/
            sbCmyk[index].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) { //SeekBar 값이 바뀌면 EditText 의 값도 바꿔주고 색상선택기 리로드
                        selectedCMYK[index] = progress;
                        changeEdtCmyk();
                        CMYKToRGB(selectedCMYK);
                        changeEdtRgb();
                        changeSbRgb();
                        changeColor();
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
           edtCmyk[index].addTextChangedListener(new TextWatcher() { //EditText 값이 바뀌면 SeekBar 의 값도 바꿔줌
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
            edtCmyk[index].setFilters(new InputFilter[]{new MinMaxFilter(0,100),new InputFilter.LengthFilter(3)}); //최소,최대값,글자수 필터 적용
            edtCmyk[index].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) { //값 입력후 Enter 입력시 색상선택기 리로드
                        changeSbCmyk();
                        CMYKToRGB(selectedCMYK);
                        changeEdtRgb();
                        changeSbRgb();
                        changeColor();
                        return true;
                    }
                    return false;
                }});
            /***** CMYK 이벤트 설정 *****/
        }
        return newTheme;
    }

    void changeColor(){
        float[] hsv = new float[3];
        int selectedColor = Color.argb(colorPickerView.getColorEnvelope().getArgb()[0],selectedRGB[1], selectedRGB[2], selectedRGB[3]);
        Color.colorToHSV(selectedColor,hsv);
        int alphaX = (int)(hsv[1]*alphaSlideBar.getMeasuredWidth());
        int brightX = (int)(hsv[2]*brightnessSlideBar.getMeasuredWidth());
        alphaSlideBar.updateSelectorX(alphaX);
        brightnessSlideBar.updateSelectorX(brightX);
        colorPickerView.selectByHsv(Color.HSVToColor(hsv));
    }

    void changeEdtRgb(){
        for (int i = 0; i < edtRgb.length; i++) {
            String temp = selectedRGB[i] + "";
            edtRgb[i].setText(temp);
        }
    }
    void changeSbRgb(){
        for (int i = 0; i < sbRgb.length; i++) {
            sbRgb[i].setProgress(selectedRGB[i]);
        }
    }
        void changeEdtCmyk(){
        for (int i = 0; i < edtCmyk.length; i++) {
            String temp = selectedCMYK[i] + "";
            edtCmyk[i].setText(temp);
        }
    }
    void changeSbCmyk(){
        for (int i = 0; i < sbCmyk.length; i++) {
            sbCmyk[i].setProgress(selectedCMYK[i]);
        }
    }

    void selectionButton(){
        for(int i = 0; i < btnColor.length; i++){
            if (colorStr[i] == null)
                btnColor[i].setText("");
            else
                btnColor[i].setText(colorStr[i]);
        }
        btnColor[selectingColorNums].setText("Choose\nColor");
    }

    //https://www.ginifab.com/feeds/pms/cmyk_to_rgb.php 인용
    void CMYKToRGB(int[] cmyk){
        //selectedRGB[0] = colorPickerView.getColorEnvelope().getArgb()[0];
        float key = ((float)cmyk[3] / 100 );
        selectedRGB[1] = (int)(255 * ( 1 - ((float)cmyk[0] / 100) ) * ( 1 - key));
        selectedRGB[2] = (int)(255 * ( 1 - ((float)cmyk[1] / 100) ) * ( 1 - key));
        selectedRGB[3] = (int)(255 * ( 1 - ((float)cmyk[2] / 100) ) * ( 1 - key));
        for (int i = 0; i <selectedRGB.length; i++){
            if (selectedRGB[i] < 0)
                selectedRGB[i] = 0;
            if (selectedRGB[i] > 255)
                selectedRGB[i] = 255;
        }
        Log.d("rgbconv", Arrays.toString(selectedRGB));
        Log.d("CMYK", Arrays.toString(selectedCMYK));
    }
    void RGBToCMYK(int[] rgb){
        float _r = (float) rgb[1] / 255.0f;
        float _g = (float) rgb[2] / 255.0f;
        float _b = (float) rgb[3] / 255.0f;

        float key = 1.0f - max(_r, _g, _b);

        selectedCMYK[0] = Math.round(((1.0f - _r - key) / (1.0f - key))*100);
        selectedCMYK[1] = Math.round(((1.0f - _g - key) / (1.0f - key))*100);
        selectedCMYK[2] = Math.round(((1.0f - _b - key) / (1.0f - key))*100);
        //selectedCMYK[3] = Math.round(key)*100;
        for (int i = 0; i <selectedCMYK.length; i++){
            if (selectedCMYK[i] < 0)
                selectedCMYK[i] = 0;
            if (selectedCMYK[i] > 100)
                selectedCMYK[i] = 100;
        }
        Log.d("rgbconv11", Arrays.toString(selectedCMYK));
    }

    private float max(float a, float b, float c)
    {
        if (a > b && a > c)
            return a;
        if (b > a && b > c)
            return b;
        if (c > a && c > b)
            return c;
        return a;
    }

}
