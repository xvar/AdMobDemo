package allgoritm.com.youla.nativead

import allgoritm.com.youla.admob.demo.BuildConfig
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import io.reactivex.subjects.PublishSubject
import java.lang.ref.WeakReference

private fun testLog(log : () -> Unit) {
    if (BuildConfig.DEBUG) {
        log()
    }
}

class AdMobNativeAdLoader(
        private val context: Context,
        private val events: PublishSubject<NativeAdEvent>,
        private val adMobPlacementManager: AdMobPlacementManager
): INativeAdLoader {

    override val canLoad: Boolean = true

    private val videoOptions = VideoOptions.Builder()
            .setClickToExpandRequested(true)
            .setStartMuted(true)
            .setCustomControlsRequested(false)
            .build()

    private val nativeAdOptions = NativeAdOptions.Builder()
            .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
            .setImageOrientation(NativeAdOptions.ORIENTATION_LANDSCAPE)
            .setVideoOptions(videoOptions)
            .build()

    private fun builder() : AdLoader.Builder {
        return AdLoader.Builder(context, adMobPlacementManager.getPlacement())
                .withNativeAdOptions(nativeAdOptions)
    }

    override fun load() {
        testLog { Log.e("ad_mob", "sending new request") }
        val listener = UnifiedListener(events)
        val loader = builder().withAdListener(listener).forUnifiedNativeAd(listener).build()
        val adRequest = AdRequest.Builder()
                .setLocation(getLocation())
                .build()
        loader.loadAd(adRequest)
    }

    private fun getLocation() : Location {
        val loc = Location("")
        //stub for demo purpose
        loc.latitude = 37.0
        loc.longitude = 54.0
        return loc
    }

    private class UnifiedListener(
            private val events: PublishSubject<NativeAdEvent>,
            private val onComplete: () -> Unit = {}
    ): AdListener(), UnifiedNativeAd.OnUnifiedNativeAdLoadedListener {

        private lateinit var adWr : WeakReference<INativeAd>

        override fun onUnifiedNativeAdLoaded(ad: UnifiedNativeAd) {
            val advert = AdMobNativeAd(ad)
            adWr = WeakReference(advert)
            events.onNext(NativeAdEvent.AdLoad(advert))
            onComplete()
            testLog {
                Log.e("ad_mob", "from = ${ad.mediationAdapterClassName} loaded add = $ad, listener instance = $this \n ${ad.str()} ")
            }
        }

        override fun onAdClicked() {
            super.onAdClicked()
            adWr.get()?.let { events.onNext(NativeAdEvent.AdClick(it)) }
        }

        override fun onAdFailedToLoad(errorCode: Int) {
            super.onAdFailedToLoad(errorCode)
            events.onNext(NativeAdEvent.NoAd(null))
            logError(errorCode)
            onComplete()
        }

        private fun logError(errorCode: Int) {
            val errorMessage = "error loading ad mob ads with code = $errorCode"
            testLog { Log.e("ad_mob", errorMessage) }
        }

        private fun UnifiedNativeAd.str() = "[ ${this.headline} ${this.body} ${this.callToAction}  ${this.advertiser} ${this.icon?.uri?.toString()} ]"

    }
}