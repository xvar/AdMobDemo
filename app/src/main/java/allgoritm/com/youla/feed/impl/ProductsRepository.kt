package allgoritm.com.youla.feed.impl

import allgoritm.com.youla.di.ScopeContainer
import allgoritm.com.youla.feed.contract.DataChange
import allgoritm.com.youla.feed.contract.SettingsProvider
import allgoritm.com.youla.models.YAdapterItem
import io.reactivex.Completable
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsRepository @Inject constructor(
    private val productGenerator: ProductGenerator,
    private val settingsProvider: SettingsProvider,
    private val scopeContainer: ScopeContainer
) {

    private val map = ConcurrentHashMap<Int, List<YAdapterItem>>()

    fun load(page: Int) : Completable {
        return Completable.fromAction {
            val list = map[page] ?: productGenerator.generateProductList(settingsProvider.getFeedPageSize())
            map[page] = list
            //emulate some backend work
            try { Thread.sleep(300) } catch (ignored: Exception) {}
            sendUpdate()
        }
    }

    fun refresh() : Completable {
        return Completable.fromAction {
            //emulate some backend work
            try { Thread.sleep(300) } catch (ignored: Exception) {}
            sendUpdate()
        }
    }

    fun clear() : Completable {
        return Completable.fromAction {
            map.clear()
            sendUpdate()
        }
    }

    private fun sendUpdate() {
        val totalList = mutableListOf<YAdapterItem>()
        val iterator = map.keys().iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            val pageList = map[key]
            if (pageList != null)
                totalList.addAll(pageList)
        }
        scopeContainer.productChanges.onNext(DataChange.Products(totalList, null))
    }

}