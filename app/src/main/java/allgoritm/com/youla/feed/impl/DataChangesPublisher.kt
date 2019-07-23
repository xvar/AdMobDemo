package allgoritm.com.youla.feed.impl

import allgoritm.com.youla.adapters.EmptyDummyItem
import allgoritm.com.youla.admob.demo.R
import allgoritm.com.youla.di.ScopeContainer
import allgoritm.com.youla.feed.contract.*
import allgoritm.com.youla.feed.model.FeedModel
import allgoritm.com.youla.feed.model.FeedState
import allgoritm.com.youla.feed.model.FeedState.FeedResult
import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.models.YAdapterItem
import allgoritm.com.youla.models.YAdapterItemFactory
import allgoritm.com.youla.utils.ResourceProvider
import allgoritm.com.youla.utils.rx.timeBoundedBuffer
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

class DataChangesPublisher @Inject constructor(
        private val rp: ResourceProvider,
        private val sc: ScopeContainer,
        private val productsRepository: ProductsRepository,
        private val itemFactory: YAdapterItemFactory
) {

    private val pageNumber = sc.page
    private val feedComposeStrategy = sc.feedComposeStrategy
    private val observerList: List<Flowable<out DataChange>> = sc.observerList
    private val feedListProxy: FeedListProxy = sc.feedListProxy

    @Suppress("UNCHECKED_CAST")
    private val dataChanges = Flowable.merge(observerList)
        .doOnNext { if (it is DataChange.Loading) lastLoadingChange.set(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .timeBoundedBuffer(100)
        .map { filterChanges(it) }
        .flatMap { Flowable.fromIterable(it) }
        .map { combineChanges(it) }
        .filter{ it is FeedResult<*> }
        .map { it as FeedResult<AdapterItem> }

    private fun filterChanges(list: List<DataChange>): LinkedHashSet<DataChange> {
        val productIndex = list.indexOfLast { it is DataChange.Products }
        val refresh = list.findLast { it is DataChange.Refresh }

        val set = LinkedHashSet<DataChange>()
        when {
            productIndex != -1 -> {
                set.addAll(list.filterIndexed { index, change ->
                    (change !is DataChange.Refresh) ||
                            (change is DataChange.Products && index == productIndex)
                })
            }
            refresh != null -> {
                set.addAll(list.filter { it !is DataChange.Refresh })
                set.add(refresh)
            }
            else -> set.addAll(list)
        }
        return set
    }

    fun getFeedChanges() : Flowable<FeedResult<AdapterItem>> = dataChanges

    private var lastLoadingChange: AtomicReference<DataChange.Loading> = AtomicReference()

    private fun combineChanges(it: DataChange): FeedState {
        val model = FeedModel(pageNumber.get())

        if (it is DataChange.Products && !it.products.isEmpty()) {
            model.products = it.products
            val lastLoadingChange = lastLoadingChange.get()
            return if (lastLoadingChange != null &&
                    lastLoadingChange is DataChange.Loading.Error &&
                    lastLoadingChange.isValid(model.pageNumber)) {
                feedWithError(feedComposeStrategy.apply(model), lastLoadingChange.error)
            } else {
                feedComposeStrategy.apply(model)
            }
        }

        if (it is DataChange.NativeAdvertHidden && it.nativeAd != null) {
            val nativeAd = it.nativeAd
            val displayedList = feedListProxy.provideList()
            displayedList?.let {
                val collection = it.minus(YAdapterItem.NativeAdvertItem(nativeAd))
                val feedResult = FeedResult<AdapterItem>()
                return feedResult.apply { items = collection }
            }
        }

        if (it is DataChange.Refresh) {
            refresh()
        }

        if (it is DataChange.Loading.Error && it.isValid(model.pageNumber)) {
            val feedResult = FeedResult<AdapterItem>().apply { items = feedListProxy.provideList() ?: emptyList() }
            return if (feedResult.items.isNotEmpty()) {
                return feedWithError(feedResult, it.error)
            } else {
                FeedState.FeedEmpty(itemFactory.getEmptyItem(it.error))
            }
        }

        if (it is DataChange.Loading.Empty && it.pageNumber == 0) {
            return FeedState.FeedEmpty(getEmptyItem())
        }

        if (it is DataChange.EmptyData) {
            model.products = emptyList()
            return feedComposeStrategy.apply(model)
        }

        return FeedState.SkipResult()
    }

    private val empty = EmptyDummyItem(R.drawable.pic_search, rp.getString(R.string.nothing_was_found), rp.getString(R.string.empty_search_description))
    private fun getEmptyItem() : EmptyDummyItem {
        return empty
    }

    private fun feedWithError(fr: FeedResult<AdapterItem>, error: Throwable) : FeedState.FeedResult<AdapterItem> {
        val collection = when {
            !fr.items.isEmpty() && fr.items[fr.items.lastIndex] !is YAdapterItem.ErrorItem -> fr.items.plus(itemFactory.getErrorItem(error))
            else -> fr.items
        }
        fr.apply { items = collection }
        fr.error = error
        return fr
    }

    private fun refresh() {
        productsRepository.refresh().subscribe()
    }

    private fun DataChange.Loading.Error.isValid(modelPageNumber: Int): Boolean {
        return pageNumber == modelPageNumber
    }

    private fun getEmptyListItem(): YAdapterItem.EmptyDummy {
        val emptyDummy = getEmptyItem()
        return YAdapterItem.EmptyDummy(emptyDummy.imageRes, emptyDummy.title, emptyDummy.description)
    }
}
