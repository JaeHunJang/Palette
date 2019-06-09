package jjh.com.palette;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private final int lastItemVerticalSpaceHeight;

    public RecyclerViewItemDecoration(int lastItemVerticalSpaceHeight) {
        this.lastItemVerticalSpaceHeight = lastItemVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //마지막 아이템만 공백 추가
        if(parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() ){
            outRect.bottom = lastItemVerticalSpaceHeight;
        }
    }
}
