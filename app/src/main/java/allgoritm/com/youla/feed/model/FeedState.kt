package allgoritm.com.youla.feed.model

import androidx.recyclerview.widget.DiffUtil
import allgoritm.com.youla.adapters.EmptyDummyItem
import allgoritm.com.youla.models.YAdapterItem


typealias FeedStateDiff = Pair<FeedState.FeedResult<YAdapterItem>, DiffUtil.DiffResult>

sealed class FeedState {

    data class Loading(val isLoading : Boolean) : FeedState()

    open class FeedResult<T> : FeedState() {
        var error: Throwable? = null
        var items: List<T> = listOf()
        open fun isEmpty() = false
        val isError
            get() = error != null
    }

    class FeedEmpty(val dummy : EmptyDummyItem) : FeedResult<YAdapterItem>() {
        override fun isEmpty(): Boolean = true
    }

    class SkipResult : FeedState()

    fun doIfError(block: (Error) -> Unit) = handle(block)

    fun doIfLoading(block: (Loading) -> Unit) = handle(block)

    fun <T> doIfFeedResult(block: (FeedResult<T>) -> Unit) = handle(block)

}

