package jjh.com.palette;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    ArrayList<RecyclerViewItems> itemList;

    public RecyclerViewAdapter(ArrayList list){
        this.itemList = list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_search, parent, false);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position ) {
        String name = itemList.get(position).getName();
        String color = itemList.get(position).getColor();
        String date = itemList.get(position).getDate();
        String tag = itemList.get(position).getTags();
        String[] colors = color.split("#");
        String[] tags = tag.split("#");

        holder.themeName.setText(name);
        holder.date.setText(date);

        for (int i = 0; i < colors.length; i++){
            if (colors[i] != null) {
                holder.colors[i].setBackgroundColor(Color.parseColor("#" + colors[i]));
                holder.colors[i].setText(colors[i]);
            }
        }
        tag = "";
        for (int i = 0; i < tags.length; i++){
            if (!tags[i].equals("")) {
                tag += " #" + tags[i];
            }
        }
        holder.tags.setText(tag);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
