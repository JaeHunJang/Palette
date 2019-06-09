package jjh.com.palette;

        import android.database.SQLException;
        import android.os.Bundle;
        import android.view.LayoutInflater;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View search = inflater.inflate(R.layout.fragment_search,container,false);

        search_rv_themeList = search.findViewById(R.id.search_rv_themeList);
        search_sp_sort = search.findViewById(R.id.search_sp_sort);
        search_chk_lib = search.findViewById(R.id.search_chk_lib);
        search_edt_keyword = search.findViewById(R.id.search_edt_keyword);

        dbHelper = new DBHelper(search.getContext());

        setRecyclerData("id like '%%'"); //모든 테마를 보여줌
        search_sp_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if (search_chk_lib.isChecked()) {
                            setRecyclerData("id = '" + Login.getInstance().getId() + "' order by date desc");
                            break;
                        }
                        setRecyclerData("id like '%%' order by date desc");
                        break;
                    case 1:
                        if (search_chk_lib.isChecked()) {
                            setRecyclerData("id = '" + Login.getInstance().getId() + "'");
                            break;
                        }
                        setRecyclerData("id like '%%'");
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return search;
    }

    @Override
    public void onResume() {
        super.onResume();
        setRecyclerData("기본 라이브러리");

    }

    void setRecyclerData(String where){
        try {
            ArrayList<RecyclerViewItems> items = new ArrayList<>();
            ArrayList[] result = dbHelper.select("Theme", where);

            recyclerViewAdapter = new RecyclerViewAdapter(items);
            for (int i = 0; i < result.length; i++) {
                if (result.length == 0)
                    break;
                items.add(new RecyclerViewItems(result[i].get(2).toString(), result[i].get(3).toString(), result[i].get(4).toString(),result[i].get(5).toString()));
            }
            search_rv_themeList.setLayoutManager(new LinearLayoutManager(getContext()));

            search_rv_themeList.setAdapter(recyclerViewAdapter);
            search_rv_themeList.addItemDecoration(new RecyclerViewItemDecoration(40));
            recyclerViewAdapter.notifyDataSetChanged();


        } catch (SQLException sqle) {
            dbHelper.getError(sqle);
        }
    }
}
