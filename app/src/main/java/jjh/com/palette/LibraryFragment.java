package jjh.com.palette;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//라이브러리에 저장된 테마들을 보는 화면
public class LibraryFragment extends Fragment {
    private RecyclerView lib_rv_themeList; //테마를 출력할 리사이클러뷰
    private Spinner lib_sp_lib; //라이브러리를 가진 스피너
    private RecyclerViewAdapter recyclerViewAdapter;
    private View dialog_newLibrary; //라이브러리 추가시 띄울 대화상자
    Vector[] result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View lib = inflater.inflate(R.layout.fragment_library, container, false);
        lib_rv_themeList = lib.findViewById(R.id.lib_rv_themeList);
        lib_sp_lib = lib.findViewById(R.id.lib_sp_lib);


        setSpinnerData();//라이브러리를 표시할 스피너를 갱신하는 메소드
        setRecyclerData("Default"); //처음엔 기본 라이브러리를 표시
        lib_sp_lib.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setRecyclerData(lib_sp_lib.getSelectedItem().toString()); //선택된 라이브러리의 색상리스트를 보여줌
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        lib.findViewById(R.id.lib_btn_addLib).setOnClickListener(new View.OnClickListener() { //라이브러리 추가 이벤트
            boolean flag = false;
            DialogInterface dlgInterface = null;

            @Override
            public void onClick(View v) {
                /***************AlertDialog 선언 및 초기화 ******************/
                final AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dialog_newLibrary = View.inflate(getContext(), R.layout.dialog_newlibrary, null); //대화상자 뷰
                final TextInputEditText dlg_fa_tit_lib; //lib를 입력받을 EditText
                final TextInputLayout dlg_fa_til_lib;
                dlg_fa_tit_lib = dialog_newLibrary.findViewById(R.id.dlg_fa_tit_lib);
                dlg_fa_til_lib = dialog_newLibrary.findViewById(R.id.dlg_fa_til_lib);

                dlg_fa_til_lib.setCounterEnabled(true);
                dlg_fa_til_lib.setCounterMaxLength(20);
                /***************AlertDialog 선언 및 초기화 ******************/

                /****************대화상자 입력 필터 시작 *******************/
                //id 의 필터
                dlg_fa_tit_lib.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //최대 길이 설정
                dlg_fa_tit_lib.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        StringChecker strChk = new StringChecker();
                        if (strChk.strPatternCheck(s)) { //TextInputEditText 가 비어있거나, 패턴에 맞으면 Error 메시지를 없앰
                            dlg_fa_til_lib.setError(null);
                            flag = true;
                        } else {
                            dlg_fa_til_lib.setError("ID는 영어와 숫자만 가능합니다.");
                            flag = false;
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                /****************대화상자 입력 필터 끝 *******************/

                dlg.setTitle("새 라이브러리 만들기") //AlertDialog 의 Builder Setting
                        .setNegativeButton("취소", null)
                        .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//추가 버튼 클릭시 라이브러리 추가 생성
                                if (flag) { //입력받은 lib 이름이 규칙에 맞으면 생성 아니면 메시지 출력
                                        Response.Listener rListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try{
                                                    JsonParser jsonParser = new JsonParser();
                                                    JsonPrimitive jsonObject = (JsonPrimitive) jsonParser.parse(response);
                                                    if (jsonObject.toString().replace("\"","").equals("overlap")) { //검색결과가 없으면 다시 입력
                                                        Toast.makeText(getContext(), "입력 정보를 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getContext(), "라이브러리가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                                                        setSpinnerData();
                                                    }


                                                }
                                                catch (Exception e){
                                                    Log.d("mytest",e.toString());
                                                }
                                            }
                                        };
                                        ValidateRequest vRequest = new ValidateRequest(Login.getInstance().getId(),dlg_fa_tit_lib.getText().toString(),rListener);
                                        RequestQueue queue = Volley.newRequestQueue(getContext());
                                        queue.add(vRequest);
                                } else
                                    Toast.makeText(getContext(), "입력정보를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setView(dialog_newLibrary)
                        .create();
                dlgInterface = dlg.show(); //Dialog 를 종료하기 위해 Interface 를 덧씌움
            }

        });
        return lib;
    }

    void setSpinnerData() { //라이브러리를 표시할 스피너를 갱신하는 메소드
            Response.Listener rListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JsonParser jsonParser = new JsonParser();
                        Log.d("aaaa",response);
                        JsonArray jsonArray = (JsonArray)jsonParser.parse(response);
                        Vector<String> spItems = new Vector<>();

                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                            spItems.add(jsonObject.get("library").toString().replace("\"",""));
                        }
                        ArrayAdapter spinnerAdpater = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spItems);
                        spinnerAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        lib_sp_lib.setAdapter(spinnerAdpater);
                        lib_sp_lib.setSelection(0);
                    }
                    catch (Exception e){
                        Log.d("mytest",e.toString());
                    }
                }
            };
            ValidateRequest vRequest = new ValidateRequest(0,Login.getInstance().getId(),rListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(vRequest);

    }

    void setRecyclerData(String lib) { //라이브러리가 가진 리스트를 출력하는 메소드

            final Vector<RecyclerViewItems> items = new Vector<>();
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
                            result[i].add(jsonObject.get("num").toString().replace("\"",""));
                            result[i].add(jsonObject.get("id").toString().replace("\"",""));
                            result[i].add(jsonObject.get("library").toString().replace("\"",""));
                            result[i].add(jsonObject.get("name").toString().replace("\"",""));
                            result[i].add(jsonObject.get("color").toString().replace("\"",""));
                            result[i].add(jsonObject.get("date").toString().replace("\"",""));
                            result[i].add(jsonObject.get("tags").toString().replace("\"",""));
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(items,getActivity());
                        for (int i = 0; i < result.length; i++) {
                            items.add(new RecyclerViewItems(result[i].get(0).toString(), result[i].get(1).toString(),result[i].get(2).toString(),
                                    result[i].get(3).toString(), result[i].get(4).toString(), result[i].get(5).toString(), result[i].get(6).toString()));
                        }
                        lib_rv_themeList.setLayoutManager(new LinearLayoutManager(getContext()));

                        lib_rv_themeList.setAdapter(recyclerViewAdapter);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e){
                        Log.d("mytest",e.toString());
                    }
                }
            };
            ValidateRequest vRequest = new ValidateRequest(Login.getInstance().getId(),2,true,lib,rListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(vRequest);

    }
}
