package allgoritm.com.youla.fragments.main.mauntable.vh

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import allgoritm.com.youla.adapters.*
import allgoritm.com.youla.adapters.EmptyDummyItem
import allgoritm.com.youla.adapters.FEED_UPDATE
import allgoritm.com.youla.adapters.REFRESH
import allgoritm.com.youla.adapters.RETRY
import allgoritm.com.youla.adapters.TOP_SCROLL
import allgoritm.com.youla.adapters.UIEvent
import allgoritm.com.youla.adapters.YUIEvent
import allgoritm.com.youla.admob.demo.R
import allgoritm.com.youla.feed.HomeVM
import allgoritm.com.youla.feed.adapter.MainAdapter
import allgoritm.com.youla.feed.contract.SettingsProvider
import allgoritm.com.youla.feed.impl.FeedAdvertScrollListener
import allgoritm.com.youla.feed.model.FeedState
import allgoritm.com.youla.feed.model.FeedStateDiff
import allgoritm.com.youla.models.AD_MOB_NATIVE_ADVERT
import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.models.EMPTY_ITEM_ID
import allgoritm.com.youla.models.ERROR_ITEM_ID
import allgoritm.com.youla.models.NATIVE_ADVERT_BLOCK
import allgoritm.com.youla.models.PRODUCT_BLOCK_AD
import allgoritm.com.youla.views.NpaGridLayoutManager
import allgoritm.com.youla.views.YRecyclerView
import allgoritm.com.youla.views.loadingRecyclerView.FeedPaginationScrollListener
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

@SuppressLint("CheckResult")
abstract class BaseFeedVh(
                 private val adapter: MainAdapter,
                 protected val viewModel: HomeVM,
                 settingsProvider: SettingsProvider
): Consumer<FeedState> {

    protected var rootView: View? = null
    protected val clicks = PublishSubject.create<UIEvent>()
    protected lateinit var rv: YRecyclerView
    protected lateinit var layoutManager: GridLayoutManager
    protected lateinit var bContainer: View

    private lateinit var feedPaginationScrollListener : FeedPaginationScrollListener

    private val pageThreshold = settingsProvider.getFeedPageThreshold()
    private val pageSize = settingsProvider.getFeedPageSize()


    init {
        clicks.subscribe { if(it.id == TOP_SCROLL) onScrollToTopEvent() }
    }

    abstract fun onCreateView(root: View)

    open fun updateDummy(dummy: EmptyDummyItem) {
        viewModel.handleEvent(YUIEvent.Base(SHOW_DUMMY))
        rv.showDummy(dummy)
    }

    fun feedItems(): MutableList<AdapterItem>? =  adapter.items
    fun firstVisiblePosition() = try {
        layoutManager.findFirstVisibleItemPosition()
    } catch (e: Exception) {
        0
    }
    fun lastVisiblePosition() = try {
        layoutManager.findLastVisibleItemPosition()
    } catch (e: Exception) {
        0
    }

    fun getOrCreateView(inflateViewFunction: () -> View): View? {
        if(rootView == null){
            rootView = inflateViewFunction()
            rv = rootView!!.findViewById(R.id.feed_rv)
            rv.hideDummy()
            onCreateView(rootView!!)
        }
        return rootView
    }

    fun setupContentView() {
        layoutManager = NpaGridLayoutManager(rootView!!.context, 2)
        layoutManager.spanSizeLookup = (object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position < 0 || adapter.itemCount == 0 || position >= adapter.itemCount)
                    return layoutManager.spanCount

                var spanCount = 1
                when (adapter.getItemViewType(position)) {
                    PRODUCT_BLOCK_AD.toInt(),
                    NATIVE_ADVERT_BLOCK.toInt(),
                    AD_MOB_NATIVE_ADVERT.toInt(),
                    ERROR_ITEM_ID.toInt(),
                    EMPTY_ITEM_ID.toInt()
                    -> spanCount = layoutManager.spanCount
                }
                return spanCount
            }
        })
        rv.setLayoutManager(layoutManager)
        rv.recyclerView.adapter = adapter
        rv.setOnRefreshListener {
            viewModel.handleEvent(YUIEvent.Base(REFRESH))
        }
        rv.setDummyButtonListener { clicks.onNext(YUIEvent.Base(RETRY)) }
        feedPaginationScrollListener = FeedPaginationScrollListener(viewModel, layoutManager, pageThreshold)
        rv.recyclerView.addOnScrollListener(feedPaginationScrollListener)
        rv.recyclerView.addOnScrollListener(
                FeedAdvertScrollListener(Consumer { t -> viewModel.handleEvent(t) }, layoutManager, pageSize)
        )
        rv.requestLayout()
    }

    fun updateFeed(pair: FeedStateDiff) {
        feedPaginationScrollListener.clear()
        rv.hideDummy()
        val diffResult = pair.second
        val items = pair.first.items
        if (items.isNotEmpty() || pair.first.isError) viewModel.handleEvent(YUIEvent.Base(FEED_UPDATE))
        adapter.items = items
        diffResult.dispatchUpdatesTo(adapter)

    }

    fun onScrollToTopEvent() = rv.recyclerView.post {
        if(layoutManager.findFirstVisibleItemPosition() != 0 ) {
            rv.recyclerView.smoothScrollToPosition(0)
        }
    }

    override fun accept(state: FeedState) {
        state.doIfLoading { rv.setRefreshing(it.isLoading) }
    }
}