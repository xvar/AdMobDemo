package com.allgoritm.youla.models.list

const val META_EMPTY = -6_000

interface AdapterItemMeta {
    val metaId: Int
}

val ITEM_META_EMPTY = object: AdapterItemMeta {
    override val metaId: Int = META_EMPTY
}