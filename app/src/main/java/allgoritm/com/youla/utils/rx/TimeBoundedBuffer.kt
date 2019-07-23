package allgoritm.com.youla.utils.rx

import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

private val EMITING_INTERVAL = 250L

fun <T> Flowable<T>.timeBoundedBuffer(bounds: Long = EMITING_INTERVAL): Flowable<List<T>> {
    return buffer(
            onBackpressureDrop()
                    .concatMap {
                        Flowable
                                .just(it)
                                .delay(bounds, TimeUnit.MILLISECONDS)
                    }
    )
            .filter { !it.isEmpty() }
}