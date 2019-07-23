package allgoritm.com.youla.feed.impl

import allgoritm.com.youla.di.ScopeContainer
import allgoritm.com.youla.feed.contract.DataChange
import allgoritm.com.youla.feed.model.FeedState
import allgoritm.com.youla.nativead.NativeAdManager
import allgoritm.com.youla.utils.rx.CompositeDisposablesMap
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class LoadingInteractor @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val nativeAdManager: NativeAdManager,
    private val scopeContainer: ScopeContainer
) {

    val loadingState = PublishProcessor.create<FeedState>()
    private val isLoading = AtomicBoolean(false)
    private val disposableMap = CompositeDisposablesMap()
    private val lastWasError = AtomicBoolean(false)
    private val key = "loading_key"

    fun loadFirst() {
        isLoading.set(false)
        scopeContainer.page.set(0)
        lastWasError.set(false)
        load(true)

    }

    fun loadNext() {
        if (!isLoading.get()) {
            load(!lastWasError.get())
        }
    }

    fun reload() {
        load(false)
    }

    private fun load(shouldIncrement: Boolean) {
        val page = if (shouldIncrement)
            scopeContainer.page.getAndIncrement()
        else
            scopeContainer.page.get()

        disposableMap.put(key,
            productsRepository.load(page)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    setLoading(true)
                    nativeAdManager.loadMore(nativeAdManager.MIN_LOAD_COUNT)
                }
                .subscribe( {
                    scopeContainer.loadingChanges.onNext(DataChange.Loading.Success())
                    lastWasError.set(false)
                    setLoading(false)
                }, {
                    setLoading(false)
                    lastWasError.set(true)
                    scopeContainer.loadingChanges.onNext(DataChange.Loading.Error(it, page))
                } )
        )
    }

    private fun setLoading(isLoading : Boolean) {
        this.isLoading.set(isLoading)
        loadingState.onNext(FeedState.Loading(isLoading))
    }
}