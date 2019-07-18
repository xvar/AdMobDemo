package allgoritm.com.youla.nativead

import com.my.target.nativeads.banners.NativePromoCard

/**
 * Created by next on 10.07.2018.
 */

interface INativeAd {

    fun hasDisclaimer(): Boolean
    fun getDisclaimer(): String?
    fun getAdvAge(): String?
    fun hasDomain(): Boolean
    fun getRating(): Float
    fun getDomain(): String?
    fun getIconUrl(): String?
    fun getButtonText(): String?
    fun getTitle(): String?
    fun getDescription(): String?

    fun getType(): NativeAdType
    fun getBannerType(): String
    fun getCards(): List<NativePromoCard> = emptyList()
}
