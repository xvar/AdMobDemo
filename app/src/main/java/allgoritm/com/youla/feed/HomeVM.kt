package allgoritm.com.youla.feed

import allgoritm.com.youla.adapters.REFRESH
import allgoritm.com.youla.adapters.UIEvent
import allgoritm.com.youla.adapters.YUIEvent
import allgoritm.com.youla.feed.contract.SettingsProvider
import allgoritm.com.youla.feed.impl.DataChangesPublisher
import allgoritm.com.youla.feed.impl.ProductsRepository
import allgoritm.com.youla.feed.model.FeedState
import allgoritm.com.youla.pagination.PaginationViewModel
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeVM @Inject constructor(
    private val changesPublisher: DataChangesPublisher,
    private val productsRepository: ProductsRepository,
    private val settingsProvider: SettingsProvider
): ViewModel(), PaginationViewModel {

    private val uiState = BehaviorProcessor.create<FeedState>()

    override fun loadFirst() {
        productsRepository.load(0)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun loadNext() {
        productsRepository.load(1) //todo
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun reload() {
        productsRepository.refresh()
            .subscribeOn(Schedulers.io())
            .subscribe()
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
        }
    }

    fun getUiState(): Flowable<FeedState> = uiState

    fun subscribeToFeed() {
        changesPublisher.getFeedChanges()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(uiState)
    }

}