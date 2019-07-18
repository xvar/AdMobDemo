package allgoritm.com.youla.adapters.viewholders

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import allgoritm.com.youla.adapters.UIEvent
import allgoritm.com.youla.adapters.delegates.YViewHolder
import allgoritm.com.youla.admob.demo.R
import allgoritm.com.youla.loader.ImageLoader
import allgoritm.com.youla.models.YAdapterItem
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import allgoritm.com.youla.views.YBadgeView
import allgoritm.com.youla.views.discount.DiscountBadgeView
import org.reactivestreams.Processor

class ProductTileViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        processor: Processor<UIEvent, UIEvent>,
        private val imageLoader: ImageLoader
) : YViewHolder<YAdapterItem.ProductTileItem>(inflater.inflate(R.layout.item_product_tile, parent, false), processor) {

    private val contentWrapper: View = itemView.findViewById(R.id.root_view)
    private val productIvId = R.id.product_iv
    private val productImageView: ImageView = itemView.findViewById(productIvId)
    private val priceTextView: TextView = itemView.findViewById(R.id.price_tv)
    private val oldPriceTextView: TextView = itemView.findViewById(R.id.old_price_tv)
    private val nameTextView: TextView = itemView.findViewById(R.id.name_tv)
    private val favoriteImageView: ImageView = itemView.findViewById(R.id.favorite_iv)
    private val badgeView: YBadgeView = itemView.findViewById(R.id.badge_view)
    private val paymentImageView: ImageView = itemView.findViewById(R.id.payment_iv)
    private val verifiedImageView: ImageView = itemView.findViewById(R.id.verified_iv)
    private val discountBadgeView: DiscountBadgeView = itemView.findViewById(R.id.discount_badge)
    private val dateTv: TextView = itemView.findViewById(R.id.date_tv)

    override fun bind(item: YAdapterItem.ProductTileItem) {
        val imageUrl = item.imageUrl

        badgeView.setupBadge(item.badge)
        badgeView.isFocusable = true
        imageLoader.loadImageTopCorners(productImageView, imageUrl)

        val discountBadgePrice = item.discountBadgePrice
        val isDiscountBadgePriceShown = !discountBadgePrice.isNullOrBlank()
        discountBadgeView.isVisible = isDiscountBadgePriceShown
        if (isDiscountBadgePriceShown) {
            discountBadgeView.setText(discountBadgePrice!!)
        }

        priceTextView.text = item.price
        val isOldPriceShown = !item.oldPrice.isNullOrBlank()
        oldPriceTextView.isVisible = isOldPriceShown
        if (isOldPriceShown) {
            oldPriceTextView.text = item.oldPrice
            oldPriceTextView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG
        }

        nameTextView.text = item.name
        paymentImageView.isVisible = item.showPayment
        verifiedImageView.isVisible = item.showVerified

        val favInfo = item.favInfo
        favoriteImageView.isVisible = favInfo.isFavIconShown
        favoriteImageView.setImageResource(favInfo.favImageResource)
        favoriteImageView.contentDescription = favInfo.favDescription
        dateTv.isVisible = item.showDate
        dateTv.text = item.date
        contentWrapper.setBackgroundResource(item.backgroundRes)
        contentWrapper.setPadding(contentWrapper.resources.getDimensionPixelSize(item.rootContentPadding))
    }

}

