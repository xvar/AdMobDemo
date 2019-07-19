package allgoritm.com.youla.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Lazy
import javax.inject.Inject

class ViewModelFactory<T : ViewModel> @Inject constructor(private val viewModel: Lazy<T>) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel.get() as T
    }

    inline fun <reified T : ViewModel> getViewModel(fragment: Fragment): T {
        val viewModelProvider = ViewModelProviders.of(fragment, this)
        return viewModelProvider[T::class.java]
    }

    inline fun <reified T : ViewModel> getViewModel(activity: FragmentActivity): T {
        val viewModelProvider = ViewModelProviders.of(activity, this)
        return viewModelProvider[T::class.java]
    }

}