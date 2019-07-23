package allgoritm.com.youla.feed

import allgoritm.com.youla.adapters.REFRESH
import allgoritm.com.youla.adapters.SETTINGS_CLICK
import allgoritm.com.youla.adapters.UIEvent
import allgoritm.com.youla.adapters.YUIEvent
import allgoritm.com.youla.di.ScopeContainer
import allgoritm.com.youla.feed.contract.DataChange
import allgoritm.com.youla.feed.contract.SettingsProvider
import allgoritm.com.youla.feed.impl.DataChangesPublisher
import allgoritm.com.youla.feed.impl.LoadingInteractor
import allgoritm.com.youla.feed.model.FeedState
import allgoritm.com.youla.models.YRouteEvent
import allgoritm.com.youla.models.route.RouteEvent
import allgoritm.com.youla.nativead.NativeAdManager
import allgoritm.com.youla.pagination.PaginationViewModel
import allgoritm.com.youla.utils.delegates.DisposableDelegate
import allgoritm.com.youla.utils.delegates.DisposableDelegateImpl
import allgoritm.com.youla.utils.rx.timeBoundedBuffer
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject

private const val KEY_RECYCLE_ADVERT = "key_recycle_advert"
private const val KEY_FEED_SUBCRIBE = "key_feed_subscribe"

class HomeVM @Inject constructor(
    private val changesPublisher: DataChangesPublisher,
    private val scopeContainer: ScopeContainer,
    private val nativeAdManager: NativeAdManager,
    private val loadingInteractor: LoadingInteractor,
    private val settingsProvider: SettingsProvider
): ViewModel(), PaginationViewModel, DisposableDelegate by DisposableDelegateImpl() {

    private val uiState = BehaviorProcessor.create<FeedState>()
    val routeEvents = PublishProcessor.create<YRouteEvent>()
    private val advertUpdatePublisher = PublishProcessor.create<UIEvent>()

    init {
        nativeAdManager.switchSession("")

        addDisposable(KEY_RECYCLE_ADVERT,
            advertUpdatePublisher
                .ofType(YUIEvent.RecycleNativeAdvert::class.java)
                .map { it.nativeAd }
                .timeBoundedBuffer(300)
                .subscribe({ list ->
                    val advertSession = nativeAdManager.getSession("")
                    for (ad in list) {
                        advertSession.recycle(ad)
                    }
                    scopeContainer.refreshChanges.onNext(DataChange.Refresh())
                } , {
                    //ignored
                })
        )

        loadFirst()
    }

    override fun loadFirst() {
        loadingInteractor.loadFirst()
    }

    override fun loadNext() {
        loadingInteractor.loadNext()
    }

    override fun reload() {
        loadingInteractor.reload()
    }

    override fun getPageSize(): Int {
        return settingsProvider.getFeedPageSize()
    }

    fun handleEvent(event: UIEvent) {
        when (event) {
            is YUIEvent.Base -> {
                when (event.id) {
                    REFRESH -> loadFirst()
                }
            }
            is YUIEvent.Click -> {
                when (event.id) {
                    SETTINGS_CLICK -> routeEvents.onNext(RouteEvent.Settings())
                }
            }
            is YUIEvent.RecycleNativeAdvert -> {
                advertUpdatePublisher.onNext(event)
            }
        }
    }

    fun getUiState(): Flowable<FeedState> = uiState.mergeWith(loadingInteractor.loadingState)

    fun subscribeToFeed() {
        changesPublisher.getFeedChanges()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(uiState)
    }

    override fun onCleared() {
        super.onCleared()
        clearAll()
    }

}