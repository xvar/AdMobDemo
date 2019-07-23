package allgoritm.com.youla.feed.impl

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import allgoritm.com.youla.feed.contract.*
import allgoritm.com.youla.feed.model.FeedModel
import allgoritm.com.youla.feed.model.FeedState.FeedResult
import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.models.YAdapterItem
import allgoritm.com.youla.models.isPromotedProduct
import allgoritm.com.youla.models.isWideItem
import allgoritm.com.youla.nativead.AdMobNativeAd
import allgoritm.com.youla.nativead.NativeAdManager
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList


class FeedComposeStrategyImpl(
        private val nativeAdManager: NativeAdManager,
        private val feedListProxy: FeedListProxy,
        private val settingsProvider: SettingsProvider
) : FeedComposeStrategy {
    override fun from(previous: FeedResult<AdapterItem>): FeedResult<AdapterItem> {
        return previous
    }

    @SuppressLint("SwitchIntDef")
    override fun apply(t: FeedModel): FeedResult<AdapterItem> {
        val mutableList = mutableListOf<AdapterItem>()

        //cut out

        t.products?.let {
            val mutableProducts = LinkedList(it)
            val productsAndAdverts = mergeProductsWithAdverts(
                    0,
                    nativeAdManager,
                    mutableProducts
            )
            mutableList.addAll(productsAndAdverts)
        }

        return FeedResult<AdapterItem>().apply { items = mutableList }
    }

    @VisibleForTesting
    fun mergeProductsWithAdverts (
            startOffsetIndex : Int,
            nativeAdManager: NativeAdManager,
            originList: LinkedList<AdapterItem>
    ): MutableList<out AdapterItem> {

        val advertSession = nativeAdManager.getSession("") //cut out, stub for demo
        advertSession.start()

        val result = mutableListOf<AdapterItem>()

        val stride = settingsProvider.getStride()

        var advertCount = 0
        var stridePos = 1

        swapPromoted(originList)

        while (!originList.isEmpty()) {
            val item : AdapterItem = originList.removeAt(0)
            when {
                item.isPromotedProduct() -> {
                    stridePos = 1
                    result.add(item)
                }
                stridePos < stride -> {
                    stridePos++
                    result.add(item)
                }
                stridePos == stride -> {
                    originList.addFirst(item)
                    stridePos = 1

                    if (canAddAdvert(advertCount)) {
                        //add advert
                        val advert = advertSession.getForPosition(result.size + startOffsetIndex, feedListProxy.getPositionData())
                        if (advert != null) {
                            val toAdd :YAdapterItem = if (advert is AdMobNativeAd)
                                YAdapterItem.AdMobNativeAdvertItem(advert)
                            else
                                YAdapterItem.NativeAdvertItem(advert)
                            result.add(toAdd)
                            advertCount++
                        }
                    }
                }
            }
        }

        swapPromoted(originList)
        return result
    }

    private fun canAddAdvert(advertCount : Int) : Boolean {
        return settingsProvider.shouldUseAdvert()
    }

    /**
     * Ensures that there is even number of items before promoted item
     * !!! Mutates original list, returns original list
     */
    @VisibleForTesting
    fun swapPromoted(items: MutableList<out AdapterItem>) : MutableList<out AdapterItem> {
        //cut out -> columns count == 2

        var index = 0
        var notPromotedCount = 0
        while (index < items.size) {
            if (!items[index].isWideItem()) {
                notPromotedCount++
            } else {
                //item is promoted
                if (notPromotedCount % 2 != 0) {
                    Collections.swap(items, index, index - 1)
                    notPromotedCount = 0
                    index--
                }
            }
            index++
        }
        return items
    }

}




