package jjh.com.palette;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView themeName;
    LinearLayout container;
    Button[] colors;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        themeName = itemView.findViewById(R.id.theme_name);
        container = itemView.findViewById(R.id.container);
        colors = new Button[5];
        colors[0] = itemView.findViewById(R.id.color1);
        colors[1] = itemView.findViewById(R.id.color2);
        colors[2] = itemView.findViewById(R.id.color3);
        colors[3] = itemView.findViewById(R.id.color4);
        colors[4] = itemView.findViewById(R.id.color5);

    }
}
