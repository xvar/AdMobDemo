package allgoritm.com.youla.adapters

import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.models.EMPTY_ITEM_ID
import allgoritm.com.youla.models.list.AdapterItemMeta

data class EmptyDummyItem(
        val imageRes: Int,
        val title: String,
        val description: String,
        val buttonText: String = ""
) : AdapterItem {

    override val meta: AdapterItemMeta

    init {
        meta = object : AdapterItemMeta {

            override val metaId = 0

        }
    }

    override fun contentsTheSame(item: AdapterItem): Boolean {
        return this == item
    }

    override fun shouldCompareContents(): Boolean {
        return false
    }

    override fun getStableId(): Long {
        return EMPTY_ITEM_ID
    }

    override fun itemsTheSame(item: AdapterItem): Boolean {
        return item is EmptyDummyItem
    }

}