package jjh.com.palette;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class UserInfoFragment extends Fragment {
    private TextView id, birth;
    private TextInputLayout til_pw, til_nick, til_hint;
    private TextInputEditText pw, nick, hint;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View userInfo = inflater.inflate(R.layout.fragment_userinfo,container,false);

        /****************선언 및 초기화 ********************************/

        Button ui_btn_update;

        id = userInfo.findViewById(R.id.ui_tv_id);
        birth = userInfo.findViewById(R.id.ui_tv_birth);
        pw = userInfo.findViewById(R.id.ui_tit_pw);
        nick = userInfo.findViewById(R.id.ui_tit_nick);
        hint = userInfo.findViewById(R.id.ui_tit_hint);
        til_pw = userInfo.findViewById(R.id.ui_til_pw);
        til_nick = userInfo.findViewById(R.id.ui_til_nick);
        til_hint = userInfo.findViewById(R.id.ui_til_hint);
        ui_btn_update = userInfo.findViewById(R.id.ui_btn_update);
        /****************선언 및 초기화 ********************************/


        /****************PW 입력 필터 시작 *******************/
        //PW 입력 처리
        pw.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern p = Pattern.compile("^[a-zA-Z0-9]+$"); //영문, 숫자 패턴
                if (source.equals("") || p.matcher(source).matches()) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    til_pw.setError(null);
                    return source;
                } else {
                    til_pw.setError("PW는 영어와 숫자만 가능합니다.");
                }
                return null;
            }
        }, new InputFilter.LengthFilter(20)}); //최대 글자수 20
        /****************PW 입력 필터 끝 *******************/

        /****************nick 입력 필터 시작 *******************/
        //PW 입력 처리
        nick.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern p = Pattern.compile("^[a-zA-Z0-9]+$"); //영문, 숫자 패턴
                if (source.equals("") || p.matcher(source).matches()) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    til_nick.setError(null);
                    return source;
                } else {
                    til_nick.setError("Nick는 영어와 숫자만 가능합니다.");
                }
                return null;
            }
        }, new InputFilter.LengthFilter(20)}); //최대 글자수 20
        /****************nick 입력 필터 끝 *******************/


        /****************hint 입력 필터 시작 *******************/
        //PW 입력 처리
        hint.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern p = Pattern.compile("^[a-zA-Z0-9]+$"); //영문, 숫자 패턴
                if (source.equals("") || p.matcher(source).matches()) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    til_hint.setError(null);
                    return source;
                } else {
                    til_hint.setError("Hint는 영어와 숫자만 가능합니다.");
                }
                return null;
            }
        }, new InputFilter.LengthFilter(20)}); //최대 글자수 20
        /****************hint 입력 필터 끝 *******************/



        /****************비밀번호 변경 확인 대화상자 시작 *******************/
        //비밀번호 변경 확인 대화상자
        ui_btn_update.setOnClickListener(new View.OnClickListener() {
            DialogInterface dlgInterface = null;

            @Override
            public void onClick(View v) {
            }
        });
        return userInfo;
    }

}
