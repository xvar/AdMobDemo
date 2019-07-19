package allgoritm.com.youla.utils.ktx

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import allgoritm.com.youla.di.ViewModelFactory
import allgoritm.com.youla.utils.ViewModelRequest

inline fun Activity?.doIfAlive(function: (Activity) -> Unit) {
    if (isAlive()) {
        function(this!!)
    }
}

inline fun Activity?.doIfHasFocus(function: (Activity) -> Unit) {
    if (hasFocus()) {
        function(this!!)
    }
}

fun Activity?.isAlive() = this != null && !isFinishing

fun Activity?.hasFocus() = this != null && !isFinishing && hasWindowFocus()

inline fun <reified T : ViewModel> ViewModelFactory<T>.get(fragment: Fragment): T = getViewModel(fragment)

inline fun <reified T : ViewModel> ViewModelFactory<T>.getForActivity(activity: FragmentActivity): T = getViewModel(activity)

inline fun <reified T : ViewModel> ViewModelFactory<T>.create(): ViewModelRequest<T> = ViewModelRequest(this, T::class.java)