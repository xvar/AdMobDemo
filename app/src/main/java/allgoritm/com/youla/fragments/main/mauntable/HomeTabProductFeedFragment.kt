package allgoritm.com.youla.fragments.main.mauntable

import allgoritm.com.youla.admob.demo.R
import allgoritm.com.youla.di.Injectable
import allgoritm.com.youla.di.ViewModelFactory
import allgoritm.com.youla.feed.HomeVM
import allgoritm.com.youla.feed.adapter.MainAdapter
import allgoritm.com.youla.feed.contract.FeedListProxy
import allgoritm.com.youla.feed.contract.SettingsProvider
import allgoritm.com.youla.feed.mapper.ProductsBaseDiff
import allgoritm.com.youla.feed.model.FeedState
import allgoritm.com.youla.feed.model.FeedStateDiff
import allgoritm.com.youla.fragments.main.mauntable.vh.BaseFeedVh
import allgoritm.com.youla.fragments.main.mauntable.vh.HomeFeedVh
import allgoritm.com.youla.loader.ImageLoader
import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.models.YAdapterItem
import allgoritm.com.youla.nativead.ListPositionData
import allgoritm.com.youla.utils.ktx.Either
import allgoritm.com.youla.utils.ktx.create
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

//cut out hierarchy

class HomeTabProductFeedFragment: MauntableFragment(), FeedListProxy, Injectable {

    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var settingsProvider: SettingsProvider
    @Inject lateinit var feedListProxy: FeedListProxy
    @Inject lateinit var hVmFactory: ViewModelFactory<HomeVM>

    private val feedDataSubscriptionKey = "pr_feed_data_sub"
    private val uiStateSubscriptionKey = "pr_ui_data_sub"

    private lateinit var feedVh : BaseFeedVh
    private lateinit var homeVM: HomeVM

    @Synchronized
    override fun provideList(): MutableList<AdapterItem>? = feedVh.feedItems()

    override fun getPositionData(): ListPositionData {
        val fp = feedVh.firstVisiblePosition()
        val lp = feedVh.lastVisiblePosition()
        return ListPositionData(fp, lp)
    }

    override fun attach(proxy: FeedListProxy) {}

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val a = activity!!

        homeVM = hVmFactory.create().of(a)

        val adapter = MainAdapter(a, imageLoader)
        adapter.setHasStableIds(true)
        feedVh = HomeFeedVh(adapter, homeVM, settingsProvider)
        putOnActivityCreatedAction {
            feedVh.setupContentView()
        }

        feedListProxy.attach(this)
        lifecycle.addObserver(feedListProxy)
        return feedVh.getOrCreateView {
            inflater.inflate(R.layout.fragment_home_product_feed, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedListProxy.attach(this)
        homeVM.subscribeToFeed()
        subscribeToUiChanges()
    }

    @Suppress("UNCHECKED_CAST")
    private fun subscribeToUiChanges() {

        val uiState = homeVM.getUiState()

        addDisposable(feedDataSubscriptionKey, uiState
                .subscribeOn(Schedulers.computation())
                .filter { state -> state is FeedState.FeedResult<*> }
                .map { state -> state as FeedState.FeedResult<*>}
                .map {
                    val value: Either<FeedState.FeedEmpty, FeedStateDiff> = if (it.isEmpty()) {
                        Either.Left(it as FeedState.FeedEmpty)
                    } else {
                        Either.Right(calculateListDiff(it as FeedState.FeedResult<YAdapterItem>))
                    }
                    value
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is Either.Right -> feedVh.updateFeed(it.right)
                        is Either.Left -> feedVh.updateDummy(it.left.dummy)
                    }
                }, { it.printStackTrace() }))

        addDisposable(uiStateSubscriptionKey, uiState
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(feedVh))

    }

    private fun calculateListDiff(it: FeedState.FeedResult<YAdapterItem>): FeedStateDiff {
        val oldItems = feedVh.feedItems() ?: emptyList<YAdapterItem>()
        val diffCallback = ProductsBaseDiff(oldItems, it.items)
        return it to DiffUtil.calculateDiff(diffCallback, true)
    }

}