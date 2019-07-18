package allgoritm.com.youla.nativead

const val ADS_MIN_CNT = 2
interface AdvertSession {

    val cachedItemsCount: Int

    val size : Int

    val maxCacheSize : Int

    fun getForPosition(pos: Int, lp: ListPositionData): INativeAd?

    fun add(nativeAd: INativeAd)

    fun reset()

    fun start()

    fun recycle(ad: INativeAd)

    fun setImplicitLoadMore(implicitLoadMore: Boolean)
}
