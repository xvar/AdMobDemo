package allgoritm.com.youla.feed.impl

import allgoritm.com.youla.admob.demo.R
import allgoritm.com.youla.feed.contract.FavoriteInfo
import allgoritm.com.youla.models.YAdapterItem
import allgoritm.com.youla.models.entity.Badge
import allgoritm.com.youla.models.list.YAdapterItemMeta
import allgoritm.com.youla.utils.ResourceProvider
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductGenerator @Inject constructor(
    private val rp: ResourceProvider
) {

    private val localIdGenerator = AtomicInteger(0)

    private val productImages = listOf(
        "https://http.cat/206.jpg",
        "https://http.cat/302.jpg",
        "https://http.cat/204.jpg",
        "https://http.cat/400.jpg",
        "https://http.cat/404.jpg",
        "https://http.cat/411.jpg",
        "https://http.cat/415.jpg",
        "https://http.cat/425.jpg",
        "https://http.cat/503.jpg",
        "https://http.cat/506.jpg",
        "https://http.cat/510.jpg",
        "https://http.cat/599.jpg",
        "https://http.cat/511.jpg",
        "https://http.cat/410.jpg",
        "https://http.cat/406.jpg",
        "https://http.cat/401.jpg",
        "https://http.cat/206.jpg",
        "https://http.cat/202.jpg",
        "https://http.cat/100.jpg"
    )
    //don't care about correct incrementing, random picture is ok
    private var imagesIndex = 0

    fun generateProductList(size: Int) : List<YAdapterItem.ProductTileItem> {
        val result = mutableListOf<YAdapterItem.ProductTileItem>()
        for (i in 0 until size) {
            result.add(generateProduct())
        }
        return result
    }

    private fun generateProduct() : YAdapterItem.ProductTileItem {
        val localId = localIdGenerator.getAndIncrement()
        val isFavorite = localId % 16 == 0
        return YAdapterItem.ProductTileItem(
            name = "product_$localId",
            localId = localId.toLong(),
            price = "1 000 rub",
            oldPrice = null,
            discountBadgePrice = null,
            imageUrl = productImages[imagesIndex++ % productImages.size],
            badge = Badge(
                title = "",
                textColor = rp.getColorStr(R.color.accent),
                backgroundColor = rp.getColorStr(R.color.white)
            ),
            distance = "1000 m",
            showPayment = localId % 2 == 0,
            showVerified = localId % 2 != 0,
            favInfo = FavoriteInfo(
                isFavorite = isFavorite,
                isFavIconShown = true,
                favImageResource = if (isFavorite) R.drawable.ic_heartfilled else R.drawable.ic_heart_outline,
                favDescription = if (isFavorite) rp.getString(R.string.fav_active) else rp.getString(R.string.fav_inactive),
                favColor = rp.getColor(R.color.alert)
            ),
            cellWidth = rp.displaySize / 2,
            date = null,
            showDate = false,
            backgroundRes = R.drawable.white_rounded_8,
            rootContentPadding = R.dimen.tile_content_padding,
            meta = YAdapterItemMeta.ProductMeta.Product(UUID.randomUUID().toString())
        )
    }
}