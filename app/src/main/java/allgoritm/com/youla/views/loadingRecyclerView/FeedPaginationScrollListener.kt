package allgoritm.com.youla.views.loadingRecyclerView

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView
import allgoritm.com.youla.pagination.PaginationViewModel

class FeedPaginationScrollListener(
        private val viewModel: PaginationViewModel,
        lm: LinearLayoutManager,
        sizeThreshold: Int
) : PaginationScrollListener(lm, viewModel.getPageSize(), sizeThreshold) {

    private var lastTotalItemNumberOnLoad : Int? = null

    fun clear() {
        lastTotalItemNumberOnLoad = null
    }

    override fun onLoadMore() {
        viewModel.loadNext()
        lastTotalItemNumberOnLoad = layoutManager.itemCount
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
        if (!(totalItemCount > 0 && firstVisibleItem > 0)) {
            return
        }

        val visibleItemCount = recyclerView.childCount
        if ((totalItemCount - visibleItemCount <= firstVisibleItem + threshold) &&
                (lastTotalItemNumberOnLoad != totalItemCount)
                && dy > 0
        ) {
            onLoadMore()
        }
    }
}