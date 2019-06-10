package jjh.com.palette;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends Fragment {
    RecyclerView search_rv_themeList;
    Spinner search_sp_sort;
    CheckBox search_chk_lib;
    EditText search_edt_keyword;
    DBHelper dbHelper;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList[] result;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View search = inflater.inflate(R.layout.fragment_search, container, false);

        search_rv_themeList = search.findViewById(R.id.search_rv_themeList);
        search_sp_sort = search.findViewById(R.id.search_sp_sort);
        search_chk_lib = search.findViewById(R.id.search_chk_lib);
        search_edt_keyword = search.findViewById(R.id.search_edt_keyword);

        dbHelper = new DBHelper(search.getContext());
        setRecyclerData("id like '%%'"); //모든 테마를 보여줌
        search_edt_keyword.setFocusable(true); //검색 이후에도 포커스를 유지하기 위한 설정

        search_edt_keyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    selectionQuery();
                    search_edt_keyword.requestFocus(); //검색이후 포커스 재설정
                }
                return false;
            }
        });
        search_sp_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectionQuery();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return search;
    }

    void selectionQuery() {
        String keyword = search_edt_keyword.getText().toString();
        switch (search_sp_sort.getSelectedItemPosition()) {
            case 0:
                if (search_chk_lib.isChecked()) {
                    setRecyclerData("name like '%" + keyword + "%' and id = '" + Login.getInstance().getId() + "' order by date desc");
                    break;
                }
                setRecyclerData("name like '%" + keyword + "%' and id like '%%' order by date desc");
                break;
            case 1:
                if (search_chk_lib.isChecked()) {
                    setRecyclerData("name like '%" + keyword + "%' and id = '" + Login.getInstance().getId() + "'");
                    break;
                }
                setRecyclerData("name like '%" + keyword + "%' and id like '%%'");
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecyclerData("library = '기본 라이브러리'");

    }

    void setRecyclerData(String where) {
        try {
            ArrayList<RecyclerViewItems> items = new ArrayList<>();
            result = dbHelper.select("Theme", where);

            recyclerViewAdapter = new RecyclerViewAdapter(items);
            for (int i = 0; i < result.length; i++) {
                if (result.length == 0)
                    break;
                items.add(new RecyclerViewItems(result[i].get(2).toString(), result[i].get(3).toString(), result[i].get(4).toString(), result[i].get(5).toString(),result[i].get(0).toString(),result[i].get(1).toString()));
            }
            search_rv_themeList.setLayoutManager(new LinearLayoutManager(getContext()));

            search_rv_themeList.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();


        } catch (SQLException sqle) {
            dbHelper.getError(sqle);
        }
    }
}
