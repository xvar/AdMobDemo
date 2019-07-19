package allgoritm.com.youla.utils

import allgoritm.com.youla.di.ViewModelFactory
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

class ViewModelRequest<T: ViewModel>(
    private val factory: ViewModelFactory<T>, private val clazz: Class<T>
) {

    fun of(fragment: Fragment) : T = ViewModelProviders.of(fragment, factory)[clazz]

    fun of(activity: FragmentActivity) : T = ViewModelProviders.of(activity, factory)[clazz]
}