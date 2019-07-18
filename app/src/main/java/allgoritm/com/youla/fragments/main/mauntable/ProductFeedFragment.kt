package allgoritm.com.youla.fragments.main.mauntable

import allgoritm.com.youla.feed.contract.FeedListProxy
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import allgoritm.com.youla.R
import allgoritm.com.youla.actions.localUserActionBuilderFromId
import allgoritm.com.youla.activities.CasaActivity
import allgoritm.com.youla.activities.main.CasaActivity
import allgoritm.com.youla.admob.demo.R
import allgoritm.com.youla.analitycs.BundleAnalyticsModel
import allgoritm.com.youla.feed.HomeVM
import allgoritm.com.youla.feed.adapter.MainAdapter
import allgoritm.com.youla.feed.contract.SettingsProvider
import allgoritm.com.youla.feed.factory.HomeVMFactory
import allgoritm.com.youla.feed.factory.MainViewModelContainer
import allgoritm.com.youla.feed.mapper.ProductsBaseDiff
import allgoritm.com.youla.feed.model.FeedState
import allgoritm.com.youla.feed.model.FeedStateDiff
import allgoritm.com.youla.fragments.main.mauntable.vh.BaseFeedVh
import allgoritm.com.youla.fragments.main.mauntable.vh.HomeFeedVh
import allgoritm.com.youla.intent.YIntent
import allgoritm.com.youla.interfaces.SubscribtionButtonListener
import allgoritm.com.youla.loader.ImageLoader
import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.models.ColumnModeProvider
import allgoritm.com.youla.models.LocalUser
import allgoritm.com.youla.models.YAdapterItem
import allgoritm.com.youla.nativead.ListPositionData
import allgoritm.com.youla.services.FavoritesService
import allgoritm.com.youla.utils.ktx.Either
import allgoritm.com.youla.utils.rx.Either
import allgoritm.com.youla.utils.rx.SchedulersTransformer
import allgoritm.com.youla.utils.ktx.uniqueLazyKey
import allgoritm.com.youla.views.UnsubscribeDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

abstract class ProductFeedFragment: MauntableFragment(), FeedListProxy {

    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var settingsProvider: SettingsProvider

    private val feedDataSubscriptionKey = "pr_feed_data_sub"
    private val uiStateSubscriptionKey = "pr_ui_data_sub"
    protected lateinit var feedVh : BaseFeedVh

    protected open lateinit var feedListProxy: FeedListProxy
    protected open lateinit var hVmFactory: HomeVMFactory
    protected open lateinit var homeVM: HomeVM

    @Synchronized
    override fun provideList(): MutableList<AdapterItem>? = feedVh.feedItems()

    override fun getPositionData(): ListPositionData {
        val fp = feedVh.firstVisiblePosition()
        val lp = feedVh.lastVisiblePosition()
        return ListPositionData(fp, lp)
    }

    override fun attach(proxy: FeedListProxy) {}

    abstract fun getSearchFilterKey(): String

    fun getVm(): HomeVM = homeVM

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        feedListProxy.attach(this)
        lifecycle.addObserver(feedListProxy)
        return feedVh.getOrCreateView {
            inflater.inflate(R.layout.fragment_home_product_feed, container, false)
        }
    }


    override fun init(a: CasaActivity) {
        super.init(a)
        val adapter = MainAdapter(a, imageLoader)
        adapter.setHasStableIds(true)
        feedVh = provideViewHolder(adapter)
        putOnActivityCreatedAction {
            feedVh.setupContentView()
        }
    }

    open fun provideViewHolder(adapter: MainAdapter): BaseFeedVh = HomeFeedVh(adapter, homeVM, settingsProvider)


    override fun mount() {
        feedListProxy.attach(this)
        //TODO упразднить необходимость порядка вызова методов
        getVm().subscribeToFeed()
        subscribeToUiChanges()
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun subscribeToUiChanges() {

        val uiState = getVm().getUiState()

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