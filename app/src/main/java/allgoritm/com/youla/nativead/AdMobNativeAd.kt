package allgoritm.com.youla.nativead


import android.webkit.URLUtil
import com.google.android.gms.ads.formats.NativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAd

data class AdMobNativeAd(val nativeAd: UnifiedNativeAd) : INativeAd {

    override fun hasDisclaimer(): Boolean = false
    override fun getDisclaimer(): String? = null
    override fun getAdvAge(): String? = "get correct age" //TODO

    override fun hasDomain(): Boolean = !nativeAd.advertiser.isNullOrBlank()
    override fun getRating(): Float = nativeAd.starRating?.toFloat() ?: 0f
    override fun getDomain(): String? = nativeAd.advertiser
    override fun getIconUrl(): String? {
        val url = nativeAd.icon?.uri?.toString()
        return if (URLUtil.isValidUrl(url)) url else null
    }

    fun getIcon(): NativeAd.Image? = nativeAd.icon

    override fun getButtonText(): String? = nativeAd.callToAction

    override fun getTitle(): String? = nativeAd.headline
    override fun getDescription(): String? = nativeAd.body

    override fun getType(): NativeAdType = NativeAdType.AD_MOB
    override fun getBannerType(): String = "banner_ad_mob ${nativeAd.mediationAdapterClassName}"

    fun isMediatedByMt(): Boolean {
        return nativeAd.mediationAdapterClassName == "com.google.ads.mediation.mytarget.MyTargetNativeAdapter"
    }

}