package jjh.com.palette;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    ArrayList<String> list;

    public RecyclerViewAdapter(ArrayList<String> list){
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search, parent, false);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position ) {

        String name = list.get(0);
        String[] colors = new String[5];
        for (int i = 0; i < colors.length; i++){
            colors[i] = list.get(i+1);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
