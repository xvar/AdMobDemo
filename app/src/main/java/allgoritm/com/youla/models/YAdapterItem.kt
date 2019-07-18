package allgoritm.com.youla.models

import allgoritm.com.youla.feed.contract.FavoriteInfo
import allgoritm.com.youla.models.entity.Badge
import allgoritm.com.youla.models.list.YAdapterItemMeta
import allgoritm.com.youla.nativead.AdMobNativeAd
import allgoritm.com.youla.nativead.INativeAd
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes

//cut out
fun AdapterItem.isWideItem() : Boolean {
    return when {
        this is YAdapterItem.NativeAdvertItem ||
        this is YAdapterItem.AdMobNativeAdvertItem -> true
        else -> false
    }
}

fun AdapterItem.isPromotedProduct() : Boolean {
    return when {
        this is YAdapterItem.NativeAdvertItem ||
                this is YAdapterItem.AdMobNativeAdvertItem -> true
        else -> false
    }
}

const val ERROR_ITEM_ID = -1034L
const val PRODUCT_TILE = -1036L
const val NATIVE_ADVERT_BLOCK = -1042L
const val PRODUCT_BLOCK_AD = -1043L
const val UNKNOWN_ID = -1048
const val AD_MOB_NATIVE_ADVERT = -1051L


sealed class YAdapterItem : AdapterItem {

    override fun contentsTheSame(item: AdapterItem): Boolean = (this == item)
    override fun shouldCompareContents() = false
    override val meta : YAdapterItemMeta = YAdapterItemMeta.EmptyMeta
    override fun getStableId() : Long = NO_STABLE_ID

    data class Header(val text: String) : YAdapterItem() {
        override fun itemsTheSame(item: AdapterItem): Boolean {
            return item is Header
        }
        override fun shouldCompareContents(): Boolean = true
    }

    data class ErrorItem(val imageRes: Int,
                         val titleRes: Int,
                         val descriptionRes: Int,
                         val buttonTextRes: Int): YAdapterItem() {
        override fun getStableId(): Long = ERROR_ITEM_ID

        override fun itemsTheSame(item: AdapterItem): Boolean {
            return item is ErrorItem && item.imageRes == imageRes
        }

    }

    data class ProductTileItem(val name: String,
                               val localId: Long,
                               val price: String,
                               val oldPrice: String?,
                               val discountBadgePrice: String?,
                               val imageUrl: String,
                               val badge: Badge,
                               val distance: String,
                               val showPayment: Boolean,
                               val showVerified: Boolean,
                               override val favInfo: FavoriteInfo,
                               val cellWidth: Int,
                               val date: String?,
                               val showDate : Boolean,
                               @DrawableRes val backgroundRes: Int,
                               @DimenRes val rootContentPadding: Int,
                               override val meta: YAdapterItemMeta.ProductMeta) : YAdapterItem(), FavInfoModel {

        override fun itemsTheSame(item: AdapterItem): Boolean {
            return (item is ProductTileItem) && this.getStableId() == item.getStableId()
        }
        override fun getStableId() = localId
        override fun shouldCompareContents(): Boolean = true
        override fun getId(): String = meta.productId()
        override fun copyWithFavInfo(newInfo: FavoriteInfo): YAdapterItem.ProductTileItem =
                this.copy(favInfo = newInfo)
    }

    interface AdvertItem {
        val item : INativeAd
    }

    data class NativeAdvertItem(override val item : INativeAd) : YAdapterItem(), AdvertItem {

        override fun itemsTheSame(item: AdapterItem): Boolean {
            return (item is NativeAdvertItem) && (this.getStableId() == item.getStableId())
        }

        override fun getStableId(): Long {
            return hashCode().toLong()
        }

        override fun shouldCompareContents(): Boolean = true

        override fun contentsTheSame(item: AdapterItem): Boolean {
            return (item is NativeAdvertItem) && this.item == item.item
        }
    }

    data class AdMobNativeAdvertItem(override val item : AdMobNativeAd) : YAdapterItem(), AdvertItem {

        override fun itemsTheSame(item: AdapterItem): Boolean {
            return (item is AdMobNativeAdvertItem) && (this.getStableId() == item.getStableId())
        }

        override fun getStableId(): Long {
            return hashCode().toLong()
        }

        override fun shouldCompareContents(): Boolean = true

        override fun contentsTheSame(item: AdapterItem): Boolean {
            return (item is AdMobNativeAdvertItem) && this.item == item.item
        }

        override val meta: YAdapterItemMeta = YAdapterItemMeta.AdMobMeta(item.getBannerType())
    }

    data class EmptyDummy(val imageRes: Int,
                          val title: String,
                          val description: String): YAdapterItem() {

        override fun itemsTheSame(item: AdapterItem): Boolean {
            return item is EmptyDummy
        }
    }
}