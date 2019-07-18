package allgoritm.com.youla.feed.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import allgoritm.com.youla.adapters.UIEvent
import allgoritm.com.youla.adapters.delegates.YListDelegate
import allgoritm.com.youla.adapters.viewholders.ProductTileViewHolder
import allgoritm.com.youla.loader.ImageLoader
import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.models.YAdapterItem
import org.reactivestreams.Processor


open class ProductTileDelegate(
        private val inflater: LayoutInflater,
        private val processor: Processor<UIEvent, UIEvent>,
        private val imageLoader: ImageLoader
) : YListDelegate<YAdapterItem.ProductTileItem, ProductTileViewHolder>() {

    override fun isForViewType(item: AdapterItem, items: List<AdapterItem>, position: Int): Boolean {
        return item is YAdapterItem.ProductTileItem
    }

    override fun onCreateViewHolder(parent: ViewGroup): ProductTileViewHolder {
        return ProductTileViewHolder(
                inflater,
                parent,
                processor,
                imageLoader
        )
    }
}
