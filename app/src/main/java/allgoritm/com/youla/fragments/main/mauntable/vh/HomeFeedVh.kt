package allgoritm.com.youla.fragments.main.mauntable.vh

import allgoritm.com.youla.feed.HomeVM
import allgoritm.com.youla.feed.adapter.MainAdapter
import allgoritm.com.youla.feed.contract.SettingsProvider
import allgoritm.com.youla.utils.ScreenUtils
import allgoritm.com.youla.views.SideOffsetItemDecoration
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HomeFeedVh(adapter: MainAdapter,
                 viewModel: HomeVM,
                 settingsProvider: SettingsProvider):
        BaseFeedVh(adapter, viewModel, settingsProvider) {


    private var position: Int = RecyclerView.NO_POSITION

    private val sideMargin = ScreenUtils.dpToPx(4)

    override fun onCreateView(root: View) {
        bContainer = rv
        rv.recyclerView.addItemDecoration(SideOffsetItemDecoration(sideMargin) {
            type -> false
        })
    }
}