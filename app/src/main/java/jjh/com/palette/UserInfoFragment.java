package jjh.com.palette;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Vector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

//회원정보를 확인하고 수정 탈퇴 로그아웃하는 화면
public class UserInfoFragment extends Fragment {
    DBHelper dbhelper;
    StringChecker strChk;
    View dlg_userInfo;
    String id, pw, birth, hint;
    boolean[] flags = {true, true};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View userInfo = inflater.inflate(R.layout.fragment_userinfo, container, false);

        /****************선언 및 초기화 ********************************/
        final Button ui_btn_update, ui_btn_logout, ui_btn_cancel;
        final TextView ui_tv_id, ui_tv_birth;
        final TextInputLayout ui_til_pw, ui_til_hint;
        final TextInputEditText ui_tit_pw, ui_tit_hint;

        dbhelper = new DBHelper(getContext());
        try {
            Vector[] result = dbhelper.select("Account", "id = '" + Login.getInstance().getId() + "'");
            id = result[0].get(0).toString();
            pw = result[0].get(1).toString();
            birth = result[0].get(2).toString();
            hint = result[0].get(3).toString();
        } catch (SQLException sqle) {
            dbhelper.getError(sqle);
        }

        ui_tv_id = userInfo.findViewById(R.id.ui_tv_id);
        ui_tv_birth = userInfo.findViewById(R.id.ui_tv_birth);
        ui_tit_pw = userInfo.findViewById(R.id.ui_tit_pw);
        ui_tit_hint = userInfo.findViewById(R.id.ui_tit_hint);

        ui_til_pw = userInfo.findViewById(R.id.ui_til_pw);
        ui_til_hint = userInfo.findViewById(R.id.ui_til_hint);

        ui_btn_update = userInfo.findViewById(R.id.ui_btn_update);
        ui_btn_logout = userInfo.findViewById(R.id.ui_btn_logout);
        ui_btn_cancel = userInfo.findViewById(R.id.ui_btn_cancel);

        ui_til_pw.setCounterEnabled(true);
        ui_til_pw.setCounterMaxLength(20);
        ui_til_pw.setPasswordVisibilityToggleEnabled(true);

        strChk = new StringChecker();

        String temp = ui_tv_id.getText().toString() + id;
        ui_tv_id.setText(temp);
        temp = ui_tv_birth.getText().toString() + id;
        ui_tv_birth.setText(temp);
        ui_tit_pw.setText(pw);
        ui_tv_birth.setText(birth);
        ui_tit_hint.setText(hint);
        /****************선언 및 초기화 ********************************/


        /****************PW 입력 필터 시작 *******************/
        //PW 입력 처리
        ui_tit_pw.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //최대 길이 20
        ui_tit_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (strChk.strPatternCheck(s)) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    ui_til_pw.setError(null);
                    flags[0] = true;

                } else {
                    ui_til_pw.setError("PW는 영어와 숫자만 가능합니다.");
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
        /****************PW 입력 필터 끝 *******************/


        /*********HINT 입력 필터 시작*************/
        //ui_tit_hint 문자 입력 필터
        ui_tit_hint.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //최대 길이 20
        ui_tit_hint.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (strChk.strPatternCheck(s)) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                    ui_til_hint.setError(null);
                    flags[1] = true;

                } else {
                    ui_til_hint.setError("Hint는 영어와 숫자만 가능합니다.");
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
        /*********HINT 입력 필터 끝*************/

        //로그아웃 버튼 이벤트
        ui_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login.getInstance().setId(null);
                Login.getInstance().setLoginState(false);
                Intent intent = new Intent(getContext(), SignInActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        //회원탈퇴 버튼 이벤트
        ui_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setTitle("회원탈퇴")
                        .setMessage("정말로 탈퇴하시겠습니까?")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    dbhelper.delete("Account", "id = '" + Login.getInstance().getId() + "'");
                                    dbhelper.delete("Library", "id = '" + Login.getInstance().getId() + "'");
                                    dbhelper.delete("Theme", "id = '" + Login.getInstance().getId() + "'");

                                } catch (SQLException sqle) {
                                    dbhelper.getError(sqle);
                                }
                                Intent intent = new Intent(getContext(), SignInActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .show();
            }
        });
        /****************비밀번호 변경 확인 대화상자 시작 *******************/
        //비밀번호 변경 확인 대화상자
        ui_btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < flags.length; i++) { //모든 조건이 만족할때까지 입력 확인
                    if (ui_tit_pw.getText().toString().equals(""))
                        ui_tit_pw.setText(pw);
                    if (ui_tit_hint.getText().toString().equals(""))
                        ui_tit_hint.setText(hint);
                    if (!flags[i]) {
                        Toast.makeText(getContext(), "입력을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                final String pw = ui_tit_pw.getText().toString(); //입력받은 ui_tit_pw
                final String hint = ui_tit_hint.getText().toString(); //입력받은 ui_tit_hint

                /***************AlertDialog 선언 및 초기화 ******************/
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg_userInfo = View.inflate(getContext(), R.layout.dialog_userinfo, null);
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

                dlg.setTitle("회원정보")
                        .setView(dlg_userInfo)
                        .setPositiveButton("수정하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    dbhelper.update("Account", "pw = '" + pw + "', hint ='" + hint + "'", "id = '" + id + "'"); //테이블에 데이터 삽입

                                } catch (SQLException sqle) {
                                    dbhelper.getError(sqle);
                                }
                                Toast.makeText(getContext(),"다시 로그인 해주세요.",Toast.LENGTH_LONG).show();
                                ui_btn_logout.callOnClick(); //회원정보가 수정되었으니 다시 로그인을 해야함
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();

            }
        });
        /****************비밀번호 변경 확인 대화상자 시작 *******************/
        return userInfo;
    }

}
