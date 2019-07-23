package allgoritm.com.youla.activities

import allgoritm.com.youla.adapters.YUIEvent
import allgoritm.com.youla.admob.demo.R
import allgoritm.com.youla.di.ViewModelFactory
import allgoritm.com.youla.feed.HomeVM
import allgoritm.com.youla.fragments.main.PreferenceFragment
import allgoritm.com.youla.fragments.main.mauntable.HomeTabProductFeedFragment
import allgoritm.com.youla.models.YRouteEvent
import allgoritm.com.youla.models.route.RouteEvent
import allgoritm.com.youla.utils.ktx.create
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class CasaActivity : BaseActivity() {

    @Inject lateinit var homeVmFactory : ViewModelFactory<HomeVM>
    lateinit var homeVM: HomeVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeVM = homeVmFactory.create().of(this)

        addDisposable("route", homeVM.routeEvents
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                //examples sake -> router handles event in production
                switchFragment(it)
            }
        )

        ic_settings.setOnClickListener {
            homeVM.handleEvent(YUIEvent.Click.Settings())
        }

        //example's sake - set initial fragment
        if (savedInstanceState == null) {
            homeVM.routeEvents.onNext(RouteEvent.Home())
        }
    }

    private fun switchFragment(event: YRouteEvent) {
        when (event) {
           is RouteEvent.Home -> {
               supportFragmentManager
                   .beginTransaction()
                   .replace(R.id.fragment_container, HomeTabProductFeedFragment())
                   .commitNowAllowingStateLoss()
           }
           is RouteEvent.Settings -> {
               supportFragmentManager
                   .beginTransaction()
                   .add(R.id.fragment_container, PreferenceFragment())
                   .addToBackStack(null)
                   .commit()
           }
        }
    }

}
