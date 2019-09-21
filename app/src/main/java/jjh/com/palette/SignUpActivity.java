package jjh.com.palette;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.Date;
import java.util.Vector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//회원가입하는 화면
public class SignUpActivity extends AppCompatActivity {
    View dlg_userInfo;
    StringChecker strChk;
    Vector[] result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /***************선언 및 초기화 ****************/
        final TextInputLayout signUp_til_id, signUp_til_pw, signUp_til_pwChk, signUp_til_hint;
        final TextInputEditText signUp_tit_id, signUp_tit_pw, signUp_tit_pwChk, signUp_tit_hint;
        final DatePicker signUp_dp_birth;
        final Button signUp_btn_signUp;
        strChk = new StringChecker();

        final boolean[] flags = {false, false, false, false};

        signUp_til_id = findViewById(R.id.signUp_til_id);
        signUp_til_pw = findViewById(R.id.signUp_til_pw);
        signUp_til_pwChk = findViewById(R.id.signUp_til_pwChk);
        signUp_til_hint = findViewById(R.id.signUp_til_hint);

        signUp_dp_birth = findViewById(R.id.signUp_dp_birth);
        signUp_btn_signUp = findViewById(R.id.signUp_btn_signUp);

        signUp_tit_id = findViewById(R.id.signUp_tit_id);
        signUp_tit_pw = findViewById(R.id.signUp_tit_pw);
        signUp_tit_pwChk = findViewById(R.id.signUp_tit_pwChk);
        signUp_tit_hint = findViewById(R.id.signUp_tit_hint);

        signUp_til_id.setCounterEnabled(true);
        signUp_til_id.setCounterMaxLength(20);

        signUp_til_pw.setCounterEnabled(true);
        signUp_til_pw.setCounterMaxLength(20);
        signUp_til_pw.setPasswordVisibilityToggleEnabled(true);

        signUp_til_pwChk.setPasswordVisibilityToggleEnabled(true);
        /***************선언 및 초기화 ****************/


        //영문, 숫자만 입력받기 참고 - https://hydok.tistory.com/17
        /*********ID 입력 필터 시작*************/
        signUp_tit_id.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});//최대 길이 20

        //문자 입력시 실행 리스너
        signUp_tit_id.addTextChangedListener(new TextWatcher() {
            //문자가 입력될때마다 ID를 DB에서 검사하여 비교
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (strChk.strPatternCheck(s)) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    signUp_til_id.setError(null);
                    flags[0] = true;
                } else {
                    flags[0] = false;
                    signUp_til_id.setError("ID는 영어와 숫자만 가능합니다.");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        /*********ID 입력 필터 끝*************/

        /*********PW 입력 필터 시작*************/
        signUp_tit_pw.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //최대 길이 20

        signUp_tit_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!signUp_tit_pwChk.getText().toString().isEmpty()) { //비밀번호 입력이 새로 들어오면 비밀번호 확인란 초기화
                    signUp_tit_pwChk.setText(null);
                } else if (strChk.strPatternCheck(s)) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    signUp_til_pw.setError(null);
                    flags[1] = true;
                } else {
                    flags[1] = false;
                    signUp_til_pw.setError("PW는 영어와 숫자만 가능합니다.");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /*********PW 입력 필터 끝*************/

        /*********PW CHECK 입력 필터 시작*************/
        //문자 입력시 실행 리스너
        signUp_tit_pwChk.addTextChangedListener(new TextWatcher() {
            //문자가 입력될때마다 ID를 DB에서 검사하여 비교
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//문자가 입력될때마다 ID를 DB에서 검사하여 비교
                if (signUp_tit_pw.getText().toString().equals(signUp_tit_pwChk.getText().toString())) {
                    signUp_til_pwChk.setError(null);
                    flags[2] = true;
                } else {
                    flags[2] = false;
                    signUp_til_pwChk.setError("PW가 다릅니다.");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        /*********PW CHECK 입력 필터 끝*************/


        /*********HINT 입력 필터 시작*************/
        //hint 문자 입력 필터
        signUp_tit_hint.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //최대 길이 20
        signUp_tit_hint.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (strChk.strPatternCheck(s)) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    signUp_til_hint.setError(null);
                    flags[3] = true;
                } else {
                    flags[3] = false;
                    signUp_til_hint.setError("Hint는 영어와 숫자만 가능합니다.");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /*********HINT 입력 필터 끝*************/

        /*********가입 버튼 이벤트 처리 시작*************/
        signUp_btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = signUp_tit_id.getText().toString(); //입력받은 id
                final String pw = signUp_tit_pw.getText().toString(); //입력받은 pw
                final String hint = signUp_tit_hint.getText().toString(); //입력받은 hint

                //날짜를 받아서 현재보다 이전 날짜인지 확인
                final String birth = String.format("%4d-%02d-%02d", signUp_dp_birth.getYear(), signUp_dp_birth.getMonth() + 1, signUp_dp_birth.getDayOfMonth()); //입력받은 생일
                Date current = new Date(); //오늘 날짜
                String today = String.format("%4d-%02d-%02d", current.getYear() + 1900, current.getMonth() + 1, current.getDate());
                if (birth.compareTo(today) >= 0) { //오늘 이전 날짜인지 비교
                    Toast.makeText(getApplicationContext(), today + " 이전 날짜를 지정해야합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < flags.length; i++) { //모든 조건이 만족할때까지 입력 확인
                    if (!flags[i]) {
                        Toast.makeText(getApplicationContext(), "입력을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                /***************AlertDialog 선언 및 초기화 ******************/
                AlertDialog.Builder dlg = new AlertDialog.Builder(SignUpActivity.this);
                dlg_userInfo = View.inflate(SignUpActivity.this, R.layout.dialog_userinfo, null);
                TextView dlg_ui_tv_id, dlg_ui_tv_birth, dlg_ui_tv_hint;
                TextInputEditText dlg_ui_tit_pw;
                TextInputLayout dlg_ui_til_pw;

                dlg_ui_tv_id = dlg_userInfo.findViewById(R.id.ui_tv_id);
                dlg_ui_tv_birth = dlg_userInfo.findViewById(R.id.ui_tv_birth);
                dlg_ui_tv_hint = dlg_userInfo.findViewById(R.id.ui_tv_hint);

                dlg_ui_tit_pw = dlg_userInfo.findViewById(R.id.ui_tit_pw);
                dlg_ui_til_pw = dlg_userInfo.findViewById(R.id.ui_til_pw);

                dlg_ui_tv_id.setText("ID : " + id);
                dlg_ui_tit_pw.setText(pw);
                dlg_ui_tv_birth.setText("BirthDay : " + birth);
                dlg_ui_tv_hint.setText("Hint : " + hint);
                dlg_ui_til_pw.setPasswordVisibilityToggleEnabled(true);
                /***************AlertDialog 선언 및 초기화 ******************/

                dlg.setTitle("가입정보")
                        .setView(dlg_userInfo)
                        .setPositiveButton("가입하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    Response.Listener rListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try{
                                                JsonParser jsonParser = new JsonParser();
                                                JsonPrimitive jsonObject = (JsonPrimitive) jsonParser.parse(response);
                                                if (jsonObject.toString().replace("\"","").equals("false")) { //검색결과가 없으면 다시 입력
                                                    Toast.makeText(SignUpActivity.this, "입력 정보를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                                                } else if(jsonObject.toString().replace("\"","").equals("overlap")) {
                                                    Toast.makeText(SignUpActivity.this, "중복되는 ID가 있습니다.", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    finish();
                                                }


                                            }
                                            catch (Exception e){
                                                Log.d("mytest",e.toString());
                                            }
                                        }
                                    };
                                    ValidateRequest vRequest = new ValidateRequest(id,pw,birth,hint,rListener);
                                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                    queue.add(vRequest);

                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
        /*********가입 버튼 이벤트 처리 끝*************/
    }
}