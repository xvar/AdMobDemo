package allgoritm.com.youla.feed.adapter

import allgoritm.com.youla.adapters.viewholders.main.ad.AdMobNativeAdvertViewHolder
import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.models.YAdapterItem
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class AdMobNativeAdvertDelegate(
        private val inflater: LayoutInflater
) : AbsListItemAdapterDelegate<YAdapterItem.AdMobNativeAdvertItem, AdapterItem, AdMobNativeAdvertViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup) = AdMobNativeAdvertViewHolder(inflater, parent)

    override fun isForViewType(item: AdapterItem, items: MutableList<AdapterItem>, position: Int) =
            item is YAdapterItem.AdMobNativeAdvertItem

    override fun onBindViewHolder(item: YAdapterItem.AdMobNativeAdvertItem,
                                  viewHolder: AdMobNativeAdvertViewHolder, payloads: MutableList<Any>) {
        viewHolder.bindAdv(item.item)
    }

    override fun onViewRecycled(viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is AdMobNativeAdvertViewHolder) {
            viewHolder.unbindAdv()
        }
        super.onViewRecycled(viewHolder)
    }
}