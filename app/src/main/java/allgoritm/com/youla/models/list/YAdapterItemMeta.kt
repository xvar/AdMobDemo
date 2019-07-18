package allgoritm.com.youla.models.list

/**
 * Created by next on 15.01.2018.
 */
const val META_AD_MOB = -6_006
const val META_PRODUCT = -6_003

sealed class YAdapterItemMeta(override val metaId : Int) : AdapterItemMeta {

    sealed class ProductMeta(override val metaId: Int): YAdapterItemMeta(metaId) {

        abstract fun productId() : String

        data class Product(
            val productId: String
        ) : ProductMeta(META_PRODUCT) {

            //cut out fields and logic

            override fun productId() = productId

            fun isPaidAd(): Boolean? {
                return false
            }
            fun isFastSell(): Boolean {
                return false
            }
        }

    }


    object EmptyMeta : YAdapterItemMeta(META_EMPTY)
    data class AdMobMeta(val bannerType: String): YAdapterItemMeta(META_AD_MOB)

}