package allgoritm.com.youla.feed

import allgoritm.com.youla.adapters.UIEvent
import allgoritm.com.youla.feed.model.FeedState
import allgoritm.com.youla.pagination.PaginationViewModel
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable

class HomeVM: ViewModel(), PaginationViewModel {
    
    override fun loadFirst() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadNext() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun reload() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPageSize(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun handleEvent(event: UIEvent) {
        
    }

    fun getUiState(): Flowable<FeedState> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun subscribeToFeed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}