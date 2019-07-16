package allgoritm.com.youla.adapters.delegates

import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

abstract class AbsListDelegate<I, T, VH, P> : AbsListItemAdapterDelegate<I, T, VH>()
    where VH : DelegateViewHolder<I, P>, I : T {

    override fun onBindViewHolder(item: I, viewHolder: VH, payloads: MutableList<Any>) {
        viewHolder.bind(item, payloads)
    }
}