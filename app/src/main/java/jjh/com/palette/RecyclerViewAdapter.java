package jjh.com.palette;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//리사이클러뷰에 탑재될 어댑터 클래스
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private Vector<RecyclerViewItems> itemList; //리사이클러뷰홀더에 탑재될 데이터를 받아옴
    private Activity activity; //대화상자를 표시할 컨택스트를 받아옴

    RecyclerViewAdapter(Vector list, Activity activity) {
        this.itemList = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.list_theme, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecyclerViewItems item = itemList.get(position);
        String num = item.getNum();
        String name = item.getName();
        String color = item.getColor();
        String date = item.getDate();
        String tag = item.getTags();
        String id = item.getId();
        String lib = item.getLib();
        String[] colors = color.split("#");
        String[] tags = tag.split("#");
        final String[] selectedItem = {num, id, lib,name, color, date, tag};
        holder.themeName.setText(name);
        holder.date.setText(date);
        holder.id.setText(id);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //아이템을 클릭하면 테마정보를 보여줌
                Intent intent = new Intent(activity, LookThemeActivity.class);
                intent.putExtra("selectedItem", selectedItem);
                activity.startActivity(intent);
            }
        });
        for (int i = 0; i < colors.length; i++) {
            if (colors[i] != null) {
                holder.colors[i].setBackgroundColor(Color.parseColor("#" + colors[i]));
                holder.colors[i].setText(colors[i]);
            }
        }
        tag = "";
        for (int i = 0; i < tags.length; i++) {
            if (!tags[i].equals("")) {
                tag += " #" + tags[i];
            }
        }
        holder.tags.setText(tag);
    }

    //구성된 아이템의 전체 크기
    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
