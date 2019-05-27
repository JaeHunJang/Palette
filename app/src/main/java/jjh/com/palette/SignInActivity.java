package jjh.com.palette;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {
    DBHelper dbhelper;
    View dialog_findaccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        /*****************선언 및 초기화***********************************/
        dbhelper = new DBHelper(this);

        //TextInputLayout, TextInputEditText 참고 - https://prince-mint.tistory.com/7
        final TextInputLayout signIn_til_id, signIn_til_pw;
        final TextInputEditText signIn_tit_id, signIn_tit_pw;
        final Button signIn_btn_signIn, signIn_btn_findID;
        signIn_btn_signIn = (findViewById(R.id.signIn_btn_signIn));
        signIn_btn_findID = (findViewById(R.id.signIn_btn_findID));

        signIn_til_id = findViewById(R.id.signIn_til_id);
        signIn_til_pw = findViewById(R.id.signIn_til_pw);

        signIn_tit_id = findViewById(R.id.signIn_tit_id);
        signIn_tit_pw = findViewById(R.id.signIn_tit_pw);

        signIn_til_id.setCounterEnabled(true);//글자수 세기 설정
        signIn_til_id.setCounterMaxLength(20); //최대 글자수 설정

        signIn_til_pw.setCounterEnabled(true);
        signIn_til_pw.setCounterMaxLength(20);
        signIn_til_pw.setPasswordVisibilityToggleEnabled(true); //비밀번호 확인하기 설정
        /****************선언 및 초기화 ********************************/

        //영문, 숫자만 입력받기 참고 - https://hydok.tistory.com/17
        /****************ID 입력 필터 시작 *******************/
        signIn_tit_id.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern p = Pattern.compile("^[a-zA-Z0-9]+$"); //영문, 숫자 패턴
                if (source.equals("") || p.matcher(source).matches()) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    signIn_til_id.setError(null);
                    return source;
                } else {
                    signIn_til_id.setError("ID는 영어와 숫자만 가능합니다.");
                }
                return null;
            }
        }, new InputFilter.LengthFilter(20)}); //최대 글자수 20
        /****************ID 입력 필터 끝 *******************/
        /****************PW 입력 필터 시작 *******************/
        //비밀번호 입력 후 Enter 누르면 로그인 버튼 Click 이벤트 실행
        signIn_tit_pw.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    signIn_btn_signIn.callOnClick();
                    return true;
                }
                return false;
            }
        });

        //PW 입력 처리
        signIn_tit_pw.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Pattern p = Pattern.compile("^[a-zA-Z0-9]+$"); //영문, 숫자 패턴
                if (source.equals("") || p.matcher(source).matches()) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    signIn_til_pw.setError(null);
                    return source;
                } else {
                    signIn_til_pw.setError("PW는 영어와 숫자만 가능합니다.");
                }
                return null;
            }
        }, new InputFilter.LengthFilter(20)}); //최대 글자수 20
        /****************PW 입력 필터 끝 *******************/

        /****************회원가입 버튼 이벤트 시작 *******************/
        //회원가입
        (findViewById(R.id.signIn_btn_signUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        /****************회원가입 버튼 이벤트 끝 *******************/

        /****************비회원 버튼 이벤트 시작 *******************/
        //비회원 이용
        (findViewById(R.id.signIn_tv_visit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, NewThemeFragment.class);
                startActivity(intent);
            }
        });
        /****************비회원 버튼 이벤트 끝 *******************/

        /****************아이디찾기 버튼 이벤트 시작 *******************/
        //아이디 찾기
        signIn_btn_findID.setOnClickListener(new View.OnClickListener() {
            DialogInterface dlgInterface = null;

            @Override
            public void onClick(View v) {
                /***************AlertDialog 선언 및 초기화 ******************/
                final AlertDialog.Builder dlg = new AlertDialog.Builder(SignInActivity.this);
                dialog_findaccount = View.inflate(SignInActivity.this, R.layout.dialog_findaccount, null);
                final TextInputEditText dlg_fa_tit_id, dlg_fa_tit_hint; //Id, hint
                final TextInputLayout dlg_fa_til_id, dlg_fa_til_hint; //id, hint 를 둘러싼 layout
                final DatePicker dlg_fa_dp_birth; //birth
                final Button dlg_fa_btn_find, dlg_fa_btn_cancel; //취소, 찾기 버튼

                dlg_fa_btn_find = dialog_findaccount.findViewById(R.id.dlg_fa_btn_find);
                dlg_fa_btn_cancel = dialog_findaccount.findViewById(R.id.dlg_fa_btn_cancel);

                dlg_fa_dp_birth = dialog_findaccount.findViewById(R.id.dlg_fa_dp_birth);
                dlg_fa_tit_id = dialog_findaccount.findViewById(R.id.dlg_fa_tit_id);
                dlg_fa_tit_hint = dialog_findaccount.findViewById(R.id.dlg_fa_tit_hint);

                dlg_fa_til_id = dialog_findaccount.findViewById(R.id.dlg_fa_til_id);
                dlg_fa_til_hint = dialog_findaccount.findViewById(R.id.dlg_fa_til_hint);

                dlg_fa_til_id.setCounterEnabled(true);
                dlg_fa_til_id.setCounterMaxLength(20);

                dlg_fa_til_hint.setCounterEnabled(true);
                dlg_fa_til_hint.setCounterMaxLength(20);

                /***************AlertDialog 선언 및 초기화 ******************/

                /****************대화상자 ID 입력 필터 시작 *******************/
                //id 의 필터
                dlg_fa_tit_id.setFilters(new InputFilter[]{new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");  //영문, 숫자 패턴
                        if (source.equals("") || p.matcher(source).matches()) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                            dlg_fa_til_id.setError(null);
                            return source;
                        } else {
                            dlg_fa_til_id.setError("ID는 영어와 숫자만 가능합니다.");
                        }
                        return null;
                    }
                }, new InputFilter.LengthFilter(20)}); //최대 길이 설정

                //문자 입력시 실행 리스너
                dlg_fa_tit_id.addTextChangedListener(new TextWatcher() {
                    //문자가 입력될때마다 ID를 DB에서 검사하여 비교
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String id = dlg_fa_tit_id.getText().toString();
                        ArrayList[] result = dbhelper.select("Account", " id=" + id);
                        if (!id.equals("") && result.length != 0) {
                            String str = result[0].get(0).toString();
                            if (str.equals(id)) {
                                dlg_fa_til_id.setError(null);
                            } else {
                                dlg_fa_til_id.setError("해당 ID가 없습니다.");
                            }
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void afterTextChanged(Editable s) {}
                });
                /****************대화상자 ID 입력 필터 끝 *******************/

                /****************대화상자 HINT 입력 필터 시작 *******************/
                //hint 의 필터
                dlg_fa_tit_hint.setFilters(new InputFilter[]{new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        Pattern p = Pattern.compile("^[a-zA-Z0-9]+$"); //영문, 숫자 패턴
                        if (source.equals("") || p.matcher(source).matches()) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                            dlg_fa_til_hint.setError(null);
                            return source;
                        } else {
                            dlg_fa_til_hint.setError("Hint는 영어와 숫자만 가능합니다.");
                        }
                        return null;
                    }
                }, new InputFilter.LengthFilter(20)}); //최대 길이를 20으로 제한
                /****************대화상자 HINT 입력 필터 끝 *******************/

                /****************대화상자 아이디찾기 버튼 이벤트 시작 *******************/
                dlg_fa_btn_find.setOnClickListener(new View.OnClickListener() { //찾기 버튼의 클릭 이벤트
                    @Override
                    public void onClick(View v) {
                        String birth = String.format("%4d-%02d-%02d", dlg_fa_dp_birth.getYear(), dlg_fa_dp_birth.getMonth() + 1, dlg_fa_dp_birth.getDayOfMonth()); //format을 통해 문자열 형식에 맞춰 만듬
                        String id = dlg_fa_tit_id.getText().toString(); //입력된 id
                        String hint = dlg_fa_tit_hint.getText().toString(); //입력된 hint
                        ArrayList[] result = dbhelper.select("Account", " id='" + id + "' and birth='" + birth + "' and hint='" + hint + "'"); //테이블에서 데이터 조회
                        if (result.length == 0) { //검색결과가 없으면 다시 입력
                            Toast.makeText(SignInActivity.this, "입력 정보를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                        } else { //검색결과가 존재하다면 pw를 0000 으로 초기화해주고 종료
                            dbhelper.update("Account", " pw = '0000' ", " id = '" + id + "'"); //테이블의 비밀번호 수정
                            signIn_tit_id.setText(result[0].get(0) + ""); //로그인화면의 아이디 자동 입력
                            Toast.makeText(SignInActivity.this, "비밀번호를 0000으로 초기화했습니다.", Toast.LENGTH_SHORT).show();
                            dlg_fa_btn_cancel.callOnClick(); //종료
                        }
                    }
                });
                /****************대화상자 아이디찾기 버튼 이벤트 시작 *******************/

                /****************대화상자 취소 버튼 이벤트 시작 *******************/
                dlg_fa_btn_cancel.setOnClickListener(new View.OnClickListener() { //취소버튼의 클릭 이벤트
                    @Override
                    public void onClick(View v) {
                        dlgInterface.dismiss(); //Dialog 종료
                    }
                });

                dlg.setTitle("회원 정보 입력") //AlertDialog 의 Builder Setting
                        .setView(dialog_findaccount)
                        .create();
                dlgInterface = dlg.show(); //Dialog 를 종료하기 위해 Interface 를 덧씌움
            }
        });
        /****************아이디찾기 버튼 이벤트 끝 *******************/


        /****************로그인 버튼 이벤트 시작 *******************/
        //로그인 성공
        signIn_btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList[] result;
                String id = signIn_tit_id.getText().toString(); //입력받은 id
                String pw = signIn_tit_pw.getText().toString(); //입력받은 pw
                if (id.isEmpty() || pw.isEmpty()) { //id와 pw 가 입력되지 않았는지 확인
                    Toast.makeText(getApplicationContext(), "일치하는 ID와 PW가 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    result = dbhelper.select("Account", " id= '" + id + "'"); //테이블에서 id 조회
                }

                if (result[0].get(0).equals(id) && result[0].get(1).equals(pw)) { //id와 pw 일치시 MainActivity 실행
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "일치하는 ID와 PW가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /****************로그인 버튼 이벤트 끝 *******************/
    }
}
