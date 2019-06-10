package jjh.com.palette;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
    ArrayList<RecyclerViewItems> itemList;
    Context context;

    public RecyclerViewAdapter(ArrayList list){
        this.itemList = list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_search, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position ) {
        RecyclerViewItems item = itemList.get(position);
        String name = item.getName();
        String color = item.getColor();
        String date = item.getDate();
        String tag = item.getTags();
        String id = item.getId();
        String lib = item.getLib();
        String[] colors = color.split("#");
        String[] tags = tag.split("#");
        final String[] selectedItem = {name,color,date,tag,id,lib};
        holder.themeName.setText(name);
        holder.date.setText(date);
        holder.id.setText(id);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LookThemeActivity.class);
                intent.putExtra("selectedItem",selectedItem);
                context.startActivity(intent);
            }
        });
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
