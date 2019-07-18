package allgoritm.com.youla.feed.contract

import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.nativead.INativeAd
import androidx.annotation.StringRes

const val ID_PRODUCT_CHANGES = 111
const val ID_ADVERT_CHANGES = 112
const val ID_ERROR_CHANGES = 113
const val ID_ADVERT_HIDE_CHANGES = 114
const val ID_ERROR_EMPTY = 115
const val ID_REFRESH = 116
const val ID_BUNDLE_IB = 117
const val ID_CAROUSELS = 118
const val ID_FAVORITES = 119
const val ID_CHAT_SIMILAR_SEND_STATES = 120
const val ID_LOADING_SUCCESS = 121
const val ID_EMPTY_DATA = 122

sealed class DataChange {
    abstract fun getId() : Int

    data class Products(val products : List<AdapterItem>, val sortOrder: String?) : DataChange() {
        override fun getId() = ID_PRODUCT_CHANGES
    }

    abstract class Loading : DataChange() {
        data class Error(val error: Throwable, val pageNumber : Int): Loading() {
            override fun getId() = ID_ERROR_CHANGES
        }
        data class Empty(val pageNumber: Int) : Loading() {
            override fun getId(): Int = ID_ERROR_EMPTY
        }
        class Success : Loading() {
            override fun getId(): Int = ID_LOADING_SUCCESS
        }
    }

    data class NativeAdvertHidden(val nativeAd: INativeAd?) : DataChange() {
        override fun getId(): Int = ID_ADVERT_HIDE_CHANGES
    }

    class NativeAdvertPageLoaded : Refresh() {
        override fun getId(): Int = ID_ADVERT_CHANGES
    }

    class BundleInfoBlockLoaded : Refresh() {
        override fun getId(): Int = ID_BUNDLE_IB
    }

    class Carousels : Refresh() {
        override fun getId(): Int = ID_CAROUSELS
    }

    open class Refresh : DataChange() {
        override fun getId(): Int = ID_REFRESH
    }

    data class Favorites(val favoriteIds: Set<String>): DataChange() {
        override fun getId(): Int = ID_FAVORITES
    }

    class EmptyData: DataChange() {
        override fun getId(): Int = ID_EMPTY_DATA
    }
}