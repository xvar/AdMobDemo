package com.allgoritm.youla.adapters

import com.allgoritm.youla.models.AdapterItem
import com.allgoritm.youla.models.EMPTY_ITEM_ID
import com.allgoritm.youla.models.list.AdapterItemMeta

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