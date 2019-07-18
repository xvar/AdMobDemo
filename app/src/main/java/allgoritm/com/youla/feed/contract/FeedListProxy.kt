package allgoritm.com.youla.feed.contract

import allgoritm.com.youla.models.AdapterItem
import allgoritm.com.youla.nativead.ListPositionData
import androidx.lifecycle.LifecycleObserver

interface FeedListProxy : LifecycleObserver {
    fun provideList() : MutableList<AdapterItem>?
    fun attach(proxy: FeedListProxy)
    fun getPositionData() : ListPositionData
}