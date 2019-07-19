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
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5c/d0/5cd04e7c0eae8d10f55c78eb.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5c/bf/5cbf6bb398200e60a47a026a.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5c/d0/5cd043800eae8d19da02ae32.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5c/d0/5cd043990eae8d19db5ee1f2.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5c/d0/5cd043990eae8d19db5ee1f2.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/1080_480/5d/1f/5d1f69790eae8d21a8698fd9.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/1080_480/5c/75/5c75168e0eae8d3daa1cc482.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5d/2f/5d2f16340eae8d2f4e663c84.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5d/13/5d135e580eae8d49532b12db.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5d/2f/5d2f15b50eae8d2fb77f592a.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5d/13/5d1391d50eae8d4664650e42.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5d/0a/5d0a082e98200e36966a62e6.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5d/0a/5d0a03a198200e214e2ad9d2.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/1080_480/5d/1f/5d1f69790eae8d21a8698fd9.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/1080_480/5c/75/5c75168e0eae8d3daa1cc482.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5d/31/5d31c7950eae8d07d9446465.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5d/28/5d286fc30eae8d74bb58dd22.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5d/28/5d286c7398200e51b73c016a.jpg",
        "https://img.youla.portal-omproxy.devmail.ru/files/images/540_540/5d/28/5d286f4d0eae8d74c376e486.jpg"
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