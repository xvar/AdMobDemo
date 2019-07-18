package allgoritm.com.youla.nativead

import allgoritm.com.youla.admob.demo.BuildConfig
import android.util.Log
import androidx.collection.LruCache
import io.reactivex.Observer
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class WindowLruAdvertSession(
        private val loadRequests: Observer<Int>,
        override val maxCacheSize: Int
) : AdvertSession {
    override val cachedItemsCount: Int
        get() = nativeAdList.size

    //load cache
    private val nativeAdList = Collections.synchronizedList(arrayListOf<INativeAd>())
    //adverts in feed
    private val SHOW_DELTA = 20
    private val advertWindow = AdvertCache(maxCacheSize)
    private val getIndex = AtomicInteger(0)
    private var implicitLoadMore: Boolean = true

    override val size: Int
        get() = advertWindow.size() + nativeAdList.size

    override fun getForPosition(pos: Int, lp: ListPositionData): INativeAd? {

        val key = getIndex.getAndIncrement()
        if (!isInRange(pos, lp)) {
            log("! in range: $pos, $lp")
            return null
        }

        val windowAd = try {
            advertWindow.get(key)?.ad
        } catch (e: Exception) {
            null
        }

        val ad = try {
            windowAd ?: nativeAdList.removeAt(0)
        } catch (e: Exception) {
            null
        }

        log("got advert = $ad")

        if (ad != null)
            advertWindow.put(key, AdvertWrapper(ad, pos))

        if (nativeAdList.size < ADS_MIN_CNT && implicitLoadMore) {
            loadRequests.onNext(ADS_MIN_CNT)
        }

        return ad
    }

    override fun setImplicitLoadMore(implicitLoadMore: Boolean) {
        this.implicitLoadMore = implicitLoadMore
    }

    private fun isInRange(pos: Int, lp: ListPositionData): Boolean {
        return !((pos < lp.firstVisiblePosition - SHOW_DELTA) || (pos > lp.lastVisiblePosition + SHOW_DELTA))
    }

    override fun add(nativeAd: INativeAd) {
        nativeAdList.add(nativeAd)
    }

    override fun reset() {
        getIndex.set(0)
        advertWindow.evictAll()
        nativeAdList.clear()
    }

    override fun start() {
        getIndex.set(0)
    }

    override fun recycle(ad: INativeAd) {
        val iterator = advertWindow.snapshot().iterator()
        var key : Int = -1
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.value?.ad == ad) {
                key = next.key
                break
            }
        }
        if (key != -1) {
            advertWindow.remove(key)
        }
    }

    private fun log(message: String) {
        if (BuildConfig.DEBUG) {
            Log.e("ad_mob", message)
        }
    }

    private data class AdvertWrapper(
            val ad: INativeAd,
            val pos: Int
    )

    private class AdvertCache(maxSize: Int) : LruCache<Int, AdvertWrapper>(maxSize) {

        override fun entryRemoved(evicted: Boolean, key: Int, oldValue: AdvertWrapper, newValue: AdvertWrapper?) {
            super.entryRemoved(evicted, key, oldValue, newValue)
            if (evicted || newValue == null) {
                val ad = oldValue.ad
                if (ad is AdMobNativeAd) {
                    ad.nativeAd.destroy()
                }
            }
        }
    }

}