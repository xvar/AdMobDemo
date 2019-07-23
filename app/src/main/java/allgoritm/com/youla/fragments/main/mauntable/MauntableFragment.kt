package allgoritm.com.youla.fragments.main.mauntable


import allgoritm.com.youla.utils.delegates.DisposableDelegate
import allgoritm.com.youla.utils.delegates.DisposableDelegateImpl
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.util.concurrent.atomic.AtomicReference

//cut out custom lifecycle
abstract class MauntableFragment : Fragment(), DisposableDelegate by DisposableDelegateImpl() {

    private val onActivityCreatedAction = AtomicReference<() -> Unit>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val a = onActivityCreatedAction.getAndSet (null )
        a?.invoke()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearAll()
    }

    fun putOnActivityCreatedAction(a: () -> Unit){
        onActivityCreatedAction.set(a)
    }

}