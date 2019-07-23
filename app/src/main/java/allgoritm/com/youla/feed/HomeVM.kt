package allgoritm.com.youla.feed

import allgoritm.com.youla.adapters.*
import allgoritm.com.youla.di.ScopeContainer
import allgoritm.com.youla.feed.contract.DataChange
import allgoritm.com.youla.feed.contract.SettingsProvider
import allgoritm.com.youla.feed.impl.DataChangesPublisher
import allgoritm.com.youla.feed.impl.LoadingInteractor
import allgoritm.com.youla.feed.model.FeedState
import allgoritm.com.youla.models.YRouteEvent
import allgoritm.com.youla.models.route.RouteEvent
import allgoritm.com.youla.nativead.NativeAdEvent
import allgoritm.com.youla.nativead.NativeAdManager
import allgoritm.com.youla.pagination.PaginationViewModel
import allgoritm.com.youla.utils.delegates.DisposableDelegate
import allgoritm.com.youla.utils.delegates.DisposableDelegateImpl
import allgoritm.com.youla.utils.rx.timeBoundedBuffer
import android.util.Log
import androidx.lifecycle.ViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val KEY_RECYCLE_ADVERT = "key_recycle_advert"
private const val KEY_UPDATE_ADVERT_FAST_SCROLL = "key_update_fast_scroll_advert"
private const val KEY_NATIVE_AD_LOAD = "key_native_ad_load"

class HomeVM @Inject constructor(
    private val changesPublisher: DataChangesPublisher,
    private val scopeContainer: ScopeContainer,
    private val nativeAdManager: NativeAdManager,
    private val loadingInteractor: LoadingInteractor,
    private val settingsProvider: SettingsProvider
): ViewModel(), PaginationViewModel, DisposableDelegate by DisposableDelegateImpl() {

    private val uiState = BehaviorProcessor.create<FeedState>()
    val routeEvents = PublishProcessor.create<YRouteEvent>()
    private val advertUpdatePublisher = BehaviorProcessor.create<UIEvent>()

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
                    ADVERT_REFRESH -> advertUpdatePublisher.onNext(event)
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
        nativeAdManager.restart()
        nativeAdManager.switchSession("")

        addDisposable(KEY_RECYCLE_ADVERT,
            advertUpdatePublisher
                .filter { settingsProvider.shouldUseAdvert() && settingsProvider.isLruSession() }
                .ofType(YUIEvent.RecycleNativeAdvert::class.java)
                .map { it.nativeAd }
                .timeBoundedBuffer(300)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list ->
                    val advertSession = nativeAdManager.getSession("")
                    for (ad in list) {
                        advertSession.recycle(ad)
                    }
                    scopeContainer.refreshChanges.onNext(DataChange.Refresh())
                } , {
                    Log.e("ViewModel", "", it)
                })
        )

        addDisposable(KEY_NATIVE_AD_LOAD,
            nativeAdManager.eventsProxy
                .filter { settingsProvider.shouldUseAdvert() && !settingsProvider.isLruSession() }
                .filter { it is NativeAdEvent.AdLoad }
                .toFlowable(BackpressureStrategy.LATEST)
                .timeBoundedBuffer(300)
                .subscribe {
                    scopeContainer.refreshChanges.onNext(DataChange.Refresh())
                }
        )

        addDisposable(KEY_UPDATE_ADVERT_FAST_SCROLL,
            advertUpdatePublisher
                .filter { it is YUIEvent.Base && it.id == ADVERT_REFRESH }
                .timeBoundedBuffer(300)
                .subscribe( {
                    scopeContainer.refreshChanges.onNext(DataChange.Refresh())
                }, {
                    Log.e("ViewModel", "", it)
                } )
        )

        changesPublisher.getFeedChanges()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(uiState)
        loadFirst()
    }

    override fun onCleared() {
        super.onCleared()
        clearAll()
    }

}