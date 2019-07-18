package allgoritm.com.youla.feed.impl

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import allgoritm.com.youla.feed.contract.FeedListProxy
import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.nativead.ListPositionData

class FeedListProxyImpl : FeedListProxy, DefaultLifecycleObserver {

    private var proxy: FeedListProxy? = null

    override fun attach(proxy: FeedListProxy) {
        this.proxy = proxy
    }

    override fun provideList(): MutableList<AdapterItem>? = proxy?.provideList()

    override fun getPositionData(): ListPositionData {
        return proxy?.getPositionData() ?: ListPositionData(0,0)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        proxy = null
    }
}