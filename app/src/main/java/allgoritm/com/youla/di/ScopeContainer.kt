package allgoritm.com.youla.di

import allgoritm.com.youla.feed.contract.DataChange
import allgoritm.com.youla.feed.contract.FeedComposeStrategy
import allgoritm.com.youla.feed.contract.FeedListProxy
import io.reactivex.processors.PublishProcessor
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

//just for the demo purposes
//production app has proper scope management
@Singleton
class ScopeContainer @Inject constructor(
    val feedComposeStrategy: FeedComposeStrategy,
    val feedListProxy : FeedListProxy
) {
    val refreshChanges: PublishProcessor<DataChange.Refresh> = PublishProcessor.create()
    val loadingChanges : PublishProcessor<DataChange.Loading> = PublishProcessor.create()
    val productChanges : PublishProcessor<DataChange.Products> = PublishProcessor.create()
    val page : AtomicInteger = AtomicInteger(0)
    val observerList = listOf(refreshChanges, loadingChanges, productChanges)
}