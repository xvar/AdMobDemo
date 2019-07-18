package allgoritm.com.youla.nativead

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Created by next on 10.07.2018.
 */
class NativeAdLoaderFactory(private val context: Context,
                            private val sp: SharedPreferences,
                            val events: PublishSubject<NativeAdEvent>
) {

    private val loaders: EnumMap<NativeAdType, INativeAdLoader?> = EnumMap(NativeAdType::class.java)
    private lateinit var currentLoaderKey: NativeAdType
    private var fallbackLoaderKey: NativeAdType? = null
    private val rwLock = ReentrantReadWriteLock()

    private val initOnce by lazy {
        init()
        true
    }

    fun init () {
        initAdMob()
        //rest cut for demonstration
    }

    private fun initAdMob() {
        rwLock.write {
            currentLoaderKey = NativeAdType.AD_MOB
            fallbackLoaderKey = null
        }
    }

    fun getLoader(): INativeAdLoader? {
        val lazyInit = initOnce
        return getLoader(currentLoaderKey)
    }

    fun getFallbackLoader(): INativeAdLoader? = getLoader(fallbackLoaderKey)

    fun switchLoader(key: NativeAdType, fallbackKey: NativeAdType?) {
        rwLock.write {
            currentLoaderKey = key
            fallbackLoaderKey = fallbackKey
        }
    }

    fun getCurrentLoaderKey() : NativeAdType = rwLock.read { currentLoaderKey }

    private fun getLoader(key: NativeAdType?) = rwLock.read {  if (key != null) loaders.getOrPut(key, { createLoader(key) }) else null }

    private fun createLoader(key: NativeAdType) = AdMobNativeAdLoader(context, events, DefaultAdMobPlacementManager())

}
