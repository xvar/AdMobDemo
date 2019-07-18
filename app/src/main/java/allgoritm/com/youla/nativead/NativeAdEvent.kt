package allgoritm.com.youla.nativead

/**
 * Created by next on 30.08.2018.
 */

const val AD_LOAD_ID = 1
const val NO_AD_ID = 2
const val SWITCH_LOADER_ID = 3
const val AD_CLICK_ID = 4
const val AD_SHOW_ID = 5
const val LOAD_FALLBACK = 6
const val LOAD_MORE = 8
const val INIT_AD_LOAD = 9

sealed class NativeAdEvent(val id: Int) {

    class AdLoad(val nativeAd: INativeAd): NativeAdEvent(AD_LOAD_ID)
    class InitAdLoad : NativeAdEvent(INIT_AD_LOAD)
    class NoAd(val nativeAd: INativeAd?): NativeAdEvent(NO_AD_ID)
    class SwitchLoader(val key: NativeAdType, val fallbackKey: NativeAdType?): NativeAdEvent(SWITCH_LOADER_ID)
    class AdClick(val nativeAd: INativeAd): NativeAdEvent(AD_CLICK_ID)
    class AdShow(val nativeAd: INativeAd): NativeAdEvent(AD_SHOW_ID)
    class LoadMore: NativeAdEvent(LOAD_MORE)
}