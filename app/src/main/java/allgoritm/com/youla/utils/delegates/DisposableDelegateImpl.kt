package allgoritm.com.youla.utils.delegates

import allgoritm.com.youla.utils.rx.CompositeDisposablesMap
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DisposableDelegateImpl : DisposableDelegate {
    private val compositeDisposable : CompositeDisposable by lazy { CompositeDisposable() }
    private val compositeDisposableMap : CompositeDisposablesMap by lazy { CompositeDisposablesMap() }

    override fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun addDisposable(key: String, disposable: Disposable) {
        compositeDisposableMap.put(key, disposable)
    }

    override fun clearDisposable(key: String) {
        compositeDisposableMap.clear(key)
    }

    override fun isDisposed(key: String) : Boolean {
        return compositeDisposableMap.isDisposed(key)
    }

    override fun clearAll() {
        compositeDisposableMap.clearAll()
        compositeDisposable.clear()
    }
}