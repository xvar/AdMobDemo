package allgoritm.com.youla.adapters.item_decorators

import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecorator(@DimenRes val offsetRes: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager as? LinearLayoutManager ?: return
        val halfOffset = view.context.resources.getDimensionPixelOffset(offsetRes) / 2
        when (layoutManager.orientation) {
            LinearLayout.VERTICAL -> {
                outRect.top = halfOffset
                outRect.bottom = halfOffset
            }
            LinearLayout.HORIZONTAL -> {
                outRect.left = halfOffset
                outRect.right = halfOffset
            }
        }
    }
}