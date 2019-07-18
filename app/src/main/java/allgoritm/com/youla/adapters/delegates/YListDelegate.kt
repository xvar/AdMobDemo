package allgoritm.com.youla.adapters.delegates

import android.view.View
import allgoritm.com.youla.adapters.UIEvent
import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.models.Payload

import org.reactivestreams.Processor

abstract class YListDelegate<I, VH> : AbsListDelegate<I, AdapterItem, VH, Payload>()
    where I : AdapterItem, VH : YViewHolder<I>

abstract class YViewHolder<T>(
        itemView: View,
        protected val processor: Processor<UIEvent, UIEvent>
) : DelegateViewHolder<T, Payload>(itemView) {

    override fun bind(payloads: List<Payload>) {
        super.bind(payloads)

        for (item in payloads) {
            handle(item)
        }
    }

    private val payloadHandlers = mutableMapOf<Int, (Payload) -> Unit>()

    protected open fun handle(item: Payload) {
        payloadHandlers[item.id]?.invoke(item)
    }

    protected fun registerHandler(id : Int, handler: (Payload) -> Unit) {
        payloadHandlers[id] = handler
    }
}