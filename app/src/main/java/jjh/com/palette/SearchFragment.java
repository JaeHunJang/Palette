package jjh.com.palette;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Vector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//테마를 검색하는 화면
public class SearchFragment extends Fragment {
    private RecyclerView search_rv_themeList; //테마를 출력할 리사이클러뷰
    private Spinner search_sp_sort; //정렬 순서를 정하는 스피너
    private CheckBox search_chk_lib; //내 라이브러리에서만 검색할건지 판단할 체크박스
    private EditText search_edt_keyword; //검색 키워드를 입력받을 뷰
    private Button search_btn_keyword;
    RecyclerViewAdapter recyclerViewAdapter;
    Vector[] result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        /*************** 선언 및 초기화 ******************/

        final View search = inflater.inflate(R.layout.fragment_search, container, false);

        search_rv_themeList = search.findViewById(R.id.search_rv_themeList);
        search_sp_sort = search.findViewById(R.id.search_sp_sort);
        search_chk_lib = search.findViewById(R.id.search_chk_lib);
        search_edt_keyword = search.findViewById(R.id.search_edt_keyword);
        search_btn_keyword=  search.findViewById(R.id.search_btn_keyword);
        setRecyclerData(); //모든 테마를 보여줌
        /*************** 선언 및 초기화 ******************/

        search_edt_keyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) { //검색 키워드 입력 후 Enter Key 입력시 검색 실행하는 이벤트
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    setRecyclerData();
                }
                return false;
            }
        });
        //정렬 기준 변경 후 이벤트
        search_sp_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setRecyclerData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //검색버튼 클릭시 이벤트
        search_btn_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRecyclerData();
            }
        });
        return search;
    }


    void setRecyclerData() { //DB 검색 결과를 리사이클러뷰에 새롭게 적용하는 메소드
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
                            if (result.length == 0)
                                break;
                            items.add(new RecyclerViewItems(result[i].get(0).toString(),result[i].get(1).toString(), result[i].get(2).toString(),
                                    result[i].get(3).toString(), result[i].get(4).toString(), result[i].get(5).toString(), result[i].get(6).toString()));
                        }
                        search_rv_themeList.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

                        search_rv_themeList.setAdapter(recyclerViewAdapter);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                    catch (Exception e){
                        Log.d("mytest",e.toString());
                    }
                }
            };
            ValidateRequest vRequest = new ValidateRequest(Login.getInstance().getId(),search_sp_sort.getSelectedItemPosition(),search_chk_lib.isChecked(),search_edt_keyword.getText().toString(),rListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(vRequest);



    }
}
