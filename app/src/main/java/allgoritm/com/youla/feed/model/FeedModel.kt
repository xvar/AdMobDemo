package allgoritm.com.youla.feed.model

import allgoritm.com.youla.models.AdapterItem

data class FeedModel(
        val pageNumber : Int,
        var products: List<AdapterItem>? = null
)