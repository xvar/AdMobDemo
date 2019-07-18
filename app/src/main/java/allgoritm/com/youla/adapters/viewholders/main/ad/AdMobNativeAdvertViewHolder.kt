package allgoritm.com.youla.adapters.viewholders.main.ad

import allgoritm.com.youla.admob.demo.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import allgoritm.com.youla.nativead.AdMobNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView

class AdMobNativeAdvertViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
) : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_native_ad_mob_block, parent, false)) {

    private val rootView = itemView.findViewById<UnifiedNativeAdView>(R.id.root)
    private val titleTextView = itemView.findViewById<TextView>(R.id.title_tv)
    private val logoImageView = itemView.findViewById<ImageView>(R.id.logo_iv)
    private val actionButton = itemView.findViewById<TextView>(R.id.action_btn)
    private val contentWrapper: ConstraintLayout = itemView.findViewById(R.id.content_wrapper)
    private val mediaAdDummy: View = itemView.findViewById(R.id.media_ad_dummy)
    private val defaultRatio = "H,324:170"
    private val mtRatio = "H,1080:607"

    private var nativeAd: AdMobNativeAd? = null

    fun unbindAdv() {}

    fun bindAdv(item: AdMobNativeAd) {
        setRatio(item)
        this.nativeAd = item
        titleTextView.text = item.getTitle()
        rootView.iconView = logoImageView

        val logoImv = rootView.iconView as ImageView?

        val icon = item.getIcon()
        logoImv?.isVisible = icon != null
        logoImv?.setImageDrawable(icon?.drawable)

        actionButton.text = item.getButtonText()
        rootView.headlineView = titleTextView
        rootView.mediaView = itemView.findViewById(R.id.media_view)
        rootView.callToActionView = actionButton

        rootView.setNativeAd(item.nativeAd)
    }

    private fun setRatio(item: AdMobNativeAd) {
        val ratio = if (item.isMediatedByMt()) mtRatio else defaultRatio
        val cs = ConstraintSet()
        cs.clone(contentWrapper)
        cs.setDimensionRatio(mediaAdDummy.id, ratio)
        cs.applyTo(contentWrapper)
    }
}
