package allgoritm.com.youla.views

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SideOffsetItemDecoration(
        private val offset: Int,
        private val wideItem: ((Int) -> Boolean)) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {

        with(outRect) {
            val position = parent.getChildAdapterPosition(view)
            val lm = parent.layoutManager
            val neededOffset =
                    if (position != RecyclerView.NO_POSITION) {
                        val viewType = parent.adapter!!.getItemViewType(position)
                        !wideItem(viewType)
                    }
                    else {
                        false
                    }
            if (neededOffset && lm is GridLayoutManager) {
                val spanCount = lm.spanCount
                val spanSizeLookup = lm.spanSizeLookup
                val spanSize = spanSizeLookup.getSpanSize(position)
                val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)
                if (spanSize == spanCount || spanIndex == 0) {
                    left = offset
                }
                if (spanSize == spanCount || spanIndex == spanCount-1) {
                    right = offset
                }
            }
            else if(neededOffset && lm is LinearLayoutManager) {
                left = offset
                right = offset
            }
        }
    }
}
