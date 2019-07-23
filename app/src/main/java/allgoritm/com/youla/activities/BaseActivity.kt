package allgoritm.com.youla.activities

import allgoritm.com.youla.utils.delegates.DisposableDelegate
import allgoritm.com.youla.utils.delegates.DisposableDelegateImpl
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector, DisposableDelegate by DisposableDelegateImpl() {
    @Inject
    lateinit var dispatchingFragmentsInjector: DispatchingAndroidInjector<Fragment>
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingFragmentsInjector

    override fun onDestroy() {
        super.onDestroy()
        clearAll()
    }
}