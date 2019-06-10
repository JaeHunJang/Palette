package jjh.com.palette;

import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryFragment extends Fragment {
    RecyclerView lib_rv_themeList;
    Spinner lib_sp_lib;
    DBHelper dbHelper;
    RecyclerViewAdapter recyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View lib = inflater.inflate(R.layout.fragment_library, container, false);
        lib_rv_themeList = lib.findViewById(R.id.lib_rv_themeList);
        lib_sp_lib = lib.findViewById(R.id.lib_sp_lib);

        dbHelper = new DBHelper(lib.getContext());


        setSpinnerData();


        setRecyclerData("기본 라이브러리");
        lib_sp_lib.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setRecyclerData(lib_sp_lib.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return lib;
    }

    @Override
    public void onResume() {
        super.onResume();
        setSpinnerData();
        setRecyclerData("기본 라이브러리");

    }

    void setSpinnerData(){
        ArrayList[] libResult = dbHelper.select("Library", "id = '" + Login.getInstance().getId() + "'");
        ArrayList<String> spItems = new ArrayList<>();
        for (ArrayList r : libResult) {
            spItems.add(r.get(1).toString());
        }
        ArrayAdapter spinnerAdpater = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spItems);
        spinnerAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lib_sp_lib.setAdapter(spinnerAdpater);
    }
    void setRecyclerData(String lib){
        try {
            ArrayList<RecyclerViewItems> items = new ArrayList<>();
            ArrayList[] result = dbHelper.select("Theme", "library = '" + lib + "' and id = '" + Login.getInstance().getId() + "'");

            recyclerViewAdapter = new RecyclerViewAdapter(items);
            for (int i = 0; i < result.length; i++) {
                if (result.length == 0)
                    break;
                items.add(new RecyclerViewItems(result[i].get(2).toString(), result[i].get(3).toString(), result[i].get(4).toString(),result[i].get(5).toString(),result[i].get(0).toString(),result[i].get(1).toString()));
            }
            lib_rv_themeList.setLayoutManager(new LinearLayoutManager(getContext()));

            lib_rv_themeList.setAdapter(recyclerViewAdapter);
            recyclerViewAdapter.notifyDataSetChanged();


        } catch (SQLException sqle) {
            dbHelper.getError(sqle);
        }
    }
}
