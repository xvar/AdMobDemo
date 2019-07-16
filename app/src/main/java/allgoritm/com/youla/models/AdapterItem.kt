package com.allgoritm.youla.models

import com.allgoritm.youla.models.list.AdapterItemMeta

const val NO_STABLE_ID = -1L
const val EMPTY_ITEM_ID = -1035L

interface AdapterItem {
    val meta: AdapterItemMeta
    fun contentsTheSame(item: AdapterItem): Boolean

    fun shouldCompareContents(): Boolean

    fun getStableId(): Long
    fun itemsTheSame(item: AdapterItem): Boolean
}