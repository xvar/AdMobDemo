package allgoritm.com.youla.fragments.main.mauntable


import allgoritm.com.youla.utils.rx.CompositeDisposablesMap
import android.os.Bundle
import androidx.fragment.app.Fragment
import io.reactivex.disposables.Disposable
import java.util.concurrent.atomic.AtomicReference

//cut out custom lifecycle
abstract class MauntableFragment : Fragment() {

    private val onActivityCreatedAction = AtomicReference<() -> Unit>()
    private val compositeDisposablesMap = CompositeDisposablesMap()

    fun addDisposable(key: String, d: Disposable) {
        compositeDisposablesMap.put(key, d)
    }

    fun clearDisposable(key: String) {
        compositeDisposablesMap.clear(key)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val a = onActivityCreatedAction.getAndSet (null )
        a?.invoke()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposablesMap.clearAll()
    }

    fun putOnActivityCreatedAction(a: () -> Unit){
        onActivityCreatedAction.set(a)
    }

}