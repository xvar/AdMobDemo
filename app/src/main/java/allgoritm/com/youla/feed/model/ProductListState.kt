package allgoritm.com.youla.feed.model

import androidx.recyclerview.widget.DiffUtil
import allgoritm.com.youla.adapters.EmptyDummyItem
import allgoritm.com.youla.models.AdapterItem

data class ProductListState(
        val isLoading: Boolean = false,
        val items: List<AdapterItem>? = null,
        val diff: DiffUtil.DiffResult? = null,
        val dummy: EmptyDummyItem? = null
) {

    fun isData() = dummy == null && items != null
    fun doIfDataDiff(block : () -> Unit) = diff?.let { block() }
    fun doIfEmpty(block : () -> Unit) = dummy?.let { block() }
}