package allgoritm.com.youla.views.loadingRecyclerView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by alexander on 10/11/16.
 */

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD = 5;

    private final int pageSize;
    protected final LinearLayoutManager layoutManager;
    private int threshold = VISIBLE_THRESHOLD;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        pageSize = 20;
    }

    public PaginationScrollListener(LinearLayoutManager layoutManager, int pageSize) {
        this.layoutManager = layoutManager;
        this.pageSize = pageSize;
    }

    public PaginationScrollListener(LinearLayoutManager layoutManager, int pageSize, int threshold) {
        this.layoutManager = layoutManager;
        this.pageSize = pageSize;
        this.threshold = threshold;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        final int visibleItemCount = recyclerView.getChildCount();
        final int totalItemCount = layoutManager.getItemCount();
        final int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if (totalItemCount >= getPageSize()
                && (totalItemCount - visibleItemCount) <= (firstVisibleItem + getThreshold())) {
            onLoadMore();
        }
    }

    protected int getThreshold() {
        return threshold;
    }

    private int getPageSize() {
        return pageSize;
    }

    public abstract void onLoadMore();

}
