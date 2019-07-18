package allgoritm.com.youla.fragments.main.mauntable


import allgoritm.com.youla.feed.contract.FeedListProxy
import javax.inject.Inject

class HomeTabProductFeedFragment: ProductFeedFragment() {

    @Inject
    override lateinit var feedListProxy: FeedListProxy

    @Inject
    override lateinit var hVmFactory: HomeVMFactory

    override fun getSearchFilterKey(): String = "home_search"

}