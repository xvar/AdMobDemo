package allgoritm.com.youla.feed

import allgoritm.com.youla.adapters.UIEvent
import allgoritm.com.youla.feed.model.FeedState
import allgoritm.com.youla.pagination.PaginationViewModel
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

class HomeVM @Inject constructor(): ViewModel(), PaginationViewModel {

    private val uiState = BehaviorProcessor.create<FeedState>()

    override fun loadFirst() {

    }

    override fun loadNext() {

    }

    override fun reload() {

    }

    override fun getPageSize(): Int {
        return 10
    }

    fun handleEvent(event: UIEvent) {
        
    }

    fun getUiState(): Flowable<FeedState> = uiState

    fun subscribeToFeed() {

    }

}