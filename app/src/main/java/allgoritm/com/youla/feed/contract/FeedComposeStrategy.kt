package allgoritm.com.youla.feed.contract

import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.feed.model.FeedModel
import allgoritm.com.youla.feed.model.FeedState.FeedResult
import io.reactivex.functions.Function

interface FeedComposeStrategy : Function<FeedModel, FeedResult<AdapterItem>> {
    fun from (previous : FeedResult<AdapterItem>) : FeedResult<AdapterItem>
}