package allgoritm.com.youla.feed.mapper

import allgoritm.com.youla.models.AdapterItem
import androidx.recyclerview.widget.DiffUtil

class ProductsBaseDiff(private val oldList: List<AdapterItem>, private val newList: List<AdapterItem>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.itemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        return if (!oldItem.shouldCompareContents()) true
               else oldItem.contentsTheSame(newList[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        /*if (oldItem is FavInfoModel && newItem is FavInfoModel) {
            return YPayload.Favorite(oldItem.favInfo, newItem.favInfo)
        }
        if (oldItem is SubscribeInfoModel && newItem is SubscribeInfoModel) {
            return YPayload.Subscribe(oldItem, newItem)
        }
        if (oldItem is YAdapterItem.CarouselItem && newItem is YAdapterItem.CarouselItem) {
            val diffCallback = ProductsBaseDiff(oldItem.productList, newItem.productList)
            val diffResult = DiffUtil.calculateDiff(diffCallback, false)
            return YPayload.Carousel(newItem, diffResult)
        }
        if (oldItem is YAdapterItem.SubscriptionsGroupItem && newItem is YAdapterItem.SubscriptionsGroupItem) {
            val diffCallback = ProductsBaseDiff(oldItem.productList, newItem.productList)
            val diffResult = DiffUtil.calculateDiff(diffCallback, false)
            return YPayload.Subscription(newItem, diffResult)
        }*/
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}
