package jjh.com.palette;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class LibraryFragment extends Fragment {
    RecyclerView lib_rv_themeList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View lib = inflater.inflate(R.layout.fragment_library,container,false);
        lib_rv_themeList = lib.findViewById(R.id.lib_rv_themeList);


        return lib;
    }
}
