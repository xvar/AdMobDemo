package allgoritm.com.youla.models

import allgoritm.com.youla.feed.contract.FavoriteInfo

interface FavInfoModel : YProduct {
    val favInfo : FavoriteInfo
    fun copyWithFavInfo(newInfo: FavoriteInfo) : YAdapterItem
}