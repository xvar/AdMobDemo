package allgoritm.com.youla.feed.impl

import allgoritm.com.youla.adapters.YUIEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import allgoritm.com.youla.adapters.ADVERT_REFRESH
import allgoritm.com.youla.adapters.UIEvent
import io.reactivex.functions.Consumer

/**
 * При резком скролле механизм обновления ленты, основанный только на recycle рекламных элементов
 * не работает. Следовательно, нужно событие, которое позволяет отследить большие участки скролла в
 * ленте.
 *
 * Данный класс при скролле, больше, чем threshold, отправляет событие о том, что нужно перекомпоновать
 * рекламу в ленте
 */
class FeedAdvertScrollListener(
        private val consumer: Consumer<UIEvent>,
        private val lm: LinearLayoutManager,
        private val threshold: Int
) : RecyclerView.OnScrollListener() {

    private var _lastScrolledPos = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val firstVisiblePos = lm.findFirstVisibleItemPosition()
        if (Math.abs(firstVisiblePos - _lastScrolledPos) > threshold) {
            _lastScrolledPos = firstVisiblePos
            consumer.accept(YUIEvent.Base(ADVERT_REFRESH))
        }
    }
}