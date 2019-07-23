package allgoritm.com.youla.utils.delegates

import io.reactivex.disposables.Disposable

interface DisposableDelegate {

    fun addDisposable(disposable: Disposable)

    fun addDisposable(key: String, disposable: Disposable)

    fun clearDisposable(key: String)

    fun isDisposed(key: String) : Boolean

    fun clearAll()

}