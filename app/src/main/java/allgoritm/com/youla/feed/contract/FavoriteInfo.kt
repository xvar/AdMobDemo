package allgoritm.com.youla.feed.contract

import androidx.annotation.DrawableRes

data class FavoriteInfo(
        val isFavorite: Boolean,
        val isFavIconShown : Boolean,
        @DrawableRes val favImageResource : Int,
        val favDescription : String,
        val favColor : Int
)