package allgoritm.com.youla.adapters.delegates

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class DelegateViewHolder<T, P>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @Suppress("UNCHECKED_CAST")
    fun bind(item: T, payloads: List<Any>) {
        if (!payloads.isNullOrEmpty()) {
            bind(payloads as List<P>)
        } else {
            bind(item)
        }
    }

    open fun bind(payloads: List<P>) {}

    abstract fun bind(item: T)
}

