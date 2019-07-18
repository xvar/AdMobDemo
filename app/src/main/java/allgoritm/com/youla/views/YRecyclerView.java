package allgoritm.com.youla.views;

import allgoritm.com.youla.admob.demo.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import allgoritm.com.youla.adapters.EmptyDummyItem;
import allgoritm.com.youla.adapters.viewholders.EmptyDummyVh;

/**
 * Created by next on 29.01.2018.
 */

public class YRecyclerView extends FrameLayout {

    public final SwipeRefreshLayout swipeRefreshLayout;

    public final RecyclerView recyclerView;
    private final View dummyView;
    private final EmptyDummyVh dummyVh;

    public YRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public YRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.y_recycler_view, this);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        dummyView = view.findViewById(R.id.dummy);
        dummyVh = new EmptyDummyVh(dummyView);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.YRecyclerView);
        int rvPaddingTop = a.getDimensionPixelSize(R.styleable.YRecyclerView_rv_padding_top, 0);
        int rvPaddingBottom = a.getDimensionPixelSize(R.styleable.YRecyclerView_rv_padding_bottom, 0);
        setRvPadding(rvPaddingTop, rvPaddingBottom);
        a.recycle();
        swipeRefreshLayout.setColorSchemeResources(R.color.accent);
    }

    public YRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs,
                         int defStyleAttr) {
        this(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public YRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs,
                         int defStyleAttr, int defStyleRes) {
        this(context, attrs);
    }

    public void setDummyButtonListener(OnClickListener listener) {
        dummyVh.getDummyButton().setOnClickListener(listener);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setRefreshingEnabled(boolean value) {
        swipeRefreshLayout.setEnabled(value);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }


    public void setRefreshing(boolean value) {
        swipeRefreshLayout.setRefreshing(value);
    }

    public void hideDummy() {
        if (recyclerView.getVisibility() != VISIBLE) {
            recyclerView.setVisibility(VISIBLE);
            dummyView.setVisibility(GONE);
        }
    }

    public void showDummy(EmptyDummyItem item) {
        dummyVh.bind(item);
        dummyView.setVisibility(VISIBLE);
        recyclerView.setVisibility(GONE);
    }

    public void setDummyTopMargin(@DimenRes int marginTop) {
        ((MarginLayoutParams) dummyView.getLayoutParams()).topMargin = getResources().getDimensionPixelOffset(marginTop);
        dummyView.requestLayout();
    }

    public void setRvPadding(int paddingTop, int paddingBottom) {
        recyclerView.setPadding(recyclerView.getPaddingLeft(), paddingTop, recyclerView.getPaddingRight(), paddingBottom);
    }

}