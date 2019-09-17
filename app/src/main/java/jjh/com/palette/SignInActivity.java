package jjh.com.palette;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.Vector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//TextInputLayout, TextInputEditText 참고 - https://prince-mint.tistory.com/7
//로그인화면
public class SignInActivity extends AppCompatActivity {
    DBHelper dbhelper;
    View dialog_findaccount;
    StringChecker strChk;
    boolean[] flags = {false, false};
    Vector[] result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        /*****************선언 및 초기화***********************************/
        dbhelper = new DBHelper(this);
        strChk = new StringChecker();

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
        signIn_tit_id.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //최대 글자수 20
        signIn_tit_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (strChk.strPatternCheck(s)) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    signIn_til_id.setError(null);
                    flags[0] = true;
                } else {
                    signIn_til_id.setError("ID는 영어와 숫자만 가능합니다.");
                    flags[0] = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        /****************ID 입력 필터 끝 *******************/
        /****************PW 입력 필터, 이벤트 시작 *******************/
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
        signIn_tit_pw.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //최대 글자수 20
        signIn_tit_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (strChk.strPatternCheck(s)) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    signIn_til_pw.setError(null);
                    flags[1] = true;
                } else {
                    signIn_til_pw.setError("PW는 영어와 숫자만 가능합니다.");
                    flags[1] = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /****************PW 입력 필터, 이벤트 끝 *******************/

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


        /****************아이디찾기 버튼 이벤트 시작 *******************/
        //아이디 찾기
        signIn_btn_findID.setOnClickListener(new View.OnClickListener() {
            DialogInterface dlgInterface = null;
            boolean[] flags = {false, false};

            @Override
            public void onClick(View v) {
                /***************AlertDialog 선언 및 초기화 ******************/
                final AlertDialog.Builder dlg = new AlertDialog.Builder(SignInActivity.this);
                dialog_findaccount = View.inflate(SignInActivity.this, R.layout.dialog_findaccount, null);
                final TextInputEditText dlg_fa_tit_id, dlg_fa_tit_hint; //Id, hint
                final TextInputLayout dlg_fa_til_id, dlg_fa_til_hint; //id, hint 를 둘러싼 list_theme
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
                dlg_fa_tit_id.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //최대 길이 설정
                dlg_fa_tit_id.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (strChk.strPatternCheck(s)) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                            dlg_fa_til_id.setError(null);
                            flags[0] = true;
                        } else {
                            dlg_fa_til_id.setError("ID는 영어와 숫자만 가능합니다.");
                            flags[0] = false;
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                /****************대화상자 ID 입력 필터 끝 *******************/

                /****************대화상자 HINT 입력 필터 시작 *******************/
                //hint 의 필터
                dlg_fa_tit_hint.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //최대 길이를 20으로 제한
                dlg_fa_tit_hint.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (strChk.strPatternCheck(s)) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                            dlg_fa_til_hint.setError(null);
                            flags[1] = true;
                        } else {
                            dlg_fa_til_hint.setError("Hint는 영어와 숫자만 가능합니다.");
                            flags[1] = false;
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                /****************대화상자 HINT 입력 필터 끝 *******************/
                /****************대화상자 아이디찾기 버튼 이벤트 시작 *******************/
                dlg_fa_btn_find.setOnClickListener(new View.OnClickListener() { //찾기 버튼의 클릭 이벤트
                    @Override
                    public void onClick(View v) {
                        for (boolean f : flags) {
                            if (!f) {
                                Toast.makeText(SignInActivity.this, "입력 정보를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        final String birth = String.format("%4d-%02d-%02d", dlg_fa_dp_birth.getYear(), dlg_fa_dp_birth.getMonth() + 1, dlg_fa_dp_birth.getDayOfMonth()); //format을 통해 문자열 형식에 맞춰 만듬
                        final String id = dlg_fa_tit_id.getText().toString(); //입력된 id
                        final String hint = dlg_fa_tit_hint.getText().toString(); //입력된 hint
                        try {
                            Response.Listener rListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JsonParser jsonParser = new JsonParser();
                                        //JsonArray jsonArray = (JsonArray)jsonParser.parse(response);
                                        result = null;
                                        //result = new Vector[jsonArray.size()];
                                        //for (int i = 0; i < jsonArray.size(); i++) {
                                            JsonPrimitive jsonObject = (JsonPrimitive) jsonParser.parse(response);
                                            //result[i] = new Vector();
                                            //result[i].add(jsonObject.get("flag").toString().replace("\"",""));
                                            //Log.d("aaaaa",result[0].get(0).toString()+" 제발");
                                        //}
                                        //Vector[] result = dbhelper.select("Account", " id='" + id + "' and birth='" + birth + "' and hint='" + hint + "'"); //테이블에서 데이터
                                        //Log.d("aaaaa",jsonObject.toString().replace("\"",""));
                                        //Log.d("aaaaa",jsonObject.toString().replace("\'","").equals("false")+"");

                                        if (jsonObject.toString().replace("\"","").equals("false")/*result[0].get(0).toString().equals("true")*/) { //검색결과가 없으면 다시 입력
                                            Toast.makeText(SignInActivity.this, "입력 정보를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                                        } else { //검색결과가 존재하다면 pw를 0000 으로 초기화해주고 종료
                                            //dbhelper.update("Account", " pw = '0000' ", " id = '" + id + "'"); //테이블의 비밀번호 수정
                                            //signIn_tit_id.setText(result[0].get(0) + ""); //로그인화면의 아이디 자동 입력
                                            Toast.makeText(SignInActivity.this, "비밀번호를 0000으로 초기화했습니다.", Toast.LENGTH_SHORT).show();
                                            dlg_fa_btn_cancel.callOnClick(); //종료
                                        }
                                    }
                                    catch (Exception e){
                                        Log.d("mytest",e.toString());
                                    }
                                }
                            };
                            ValidateRequest vRequest = new ValidateRequest(id,birth,hint,rListener);
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            queue.add(vRequest);
                            /*
                            Vector[] result = dbhelper.select("Account", " id='" + id + "' and birth='" + birth + "' and hint='" + hint + "'"); //테이블에서 데이터 조회
                            if (result.length == 0) { //검색결과가 없으면 다시 입력
                                Toast.makeText(SignInActivity.this, "입력 정보를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                            } else { //검색결과가 존재하다면 pw를 0000 으로 초기화해주고 종료
                                dbhelper.update("Account", " pw = '0000' ", " id = '" + id + "'"); //테이블의 비밀번호 수정
                                signIn_tit_id.setText(result[0].get(0) + ""); //로그인화면의 아이디 자동 입력
                                Toast.makeText(SignInActivity.this, "비밀번호를 0000으로 초기화했습니다.", Toast.LENGTH_SHORT).show();
                                dlg_fa_btn_cancel.callOnClick(); //종료
                            }*/
                            /*if (result[0].get(0).toString().equals("true")){
                                signIn_tit_id.setText(id); //로그인화면의 아이디 자동 입력
                                Toast.makeText(getApplicationContext(), "비밀번호를 0000으로 초기화했습니다.", Toast.LENGTH_SHORT).show();
                                dlg_fa_btn_cancel.callOnClick(); //종료
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "입력 정보를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                            }*/
                        } catch (SQLException sqle) {
                            dbhelper.getError(sqle);
                        }
                    }
                });
                /****************대화상자 아이디찾기 버튼 이벤트 끝 *******************/

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
                for (boolean f : flags) {
                    if (!f) {
                        Toast.makeText(SignInActivity.this, "입력 정보를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                final String id = signIn_tit_id.getText().toString(); //입력받은 id
                final String pw = signIn_tit_pw.getText().toString(); //입력받은 pw
                try {
                    Response.Listener rListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JsonParser jsonParser = new JsonParser();
                                JsonArray jsonArray = (JsonArray)jsonParser.parse(response);
                                result = null;
                                result = new Vector[jsonArray.size()];
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                                    result[i] = new Vector();
                                    result[i].add(jsonObject.get("id").toString().replace("\"",""));
                                    result[i].add(jsonObject.get("pw").toString().replace("\"",""));
                                    result[i].add(jsonObject.get("birth").toString().replace("\"",""));
                                    result[i].add(jsonObject.get("hint").toString().replace("\"",""));
                                }
                                if (result.length == 0) { //id가 존재하는 지 확인
                                    Toast.makeText(getApplicationContext(), "일치하는 ID와 PW가 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                                if (result[0].get(0).equals(id) && result[0].get(1).equals(pw)) { //id와 pw 일치시 MainActivity 실행
                                    Login login = Login.getInstance();
                                    login.setId(id+"");
                                    login.setLoginState(true);
                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //이전에 실행된 메인 화면을 종료함
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "일치하는 ID와 PW가 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (Exception e){
                                Log.d("mytest",e.toString());
                            }
                        }
                    };
                    ValidateRequest vRequest = new ValidateRequest(id,rListener);
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(vRequest);

                    /*result = dbhelper.select("Account", " id= '" + id + "'"); //테이블에서 id 조회
                    if (result.length == 0) { //id가 존재하는 지 확인
                        Toast.makeText(getApplicationContext(), "일치하는 ID와 PW가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (result[0].get(0).equals(id) && result[0].get(1).equals(pw)) { //id와 pw 일치시 MainActivity 실행
                        Login login = Login.getInstance();
                        login.setId(id+"");
                        login.setLoginState(true);
                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //이전에 실행된 메인 화면을 종료함
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "일치하는 ID와 PW가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }*/
                } catch (SQLException sqle) {
                    dbhelper.getError(sqle);
                }
            }
        });
        /****************로그인 버튼 이벤트 끝 *******************/
    }
}
