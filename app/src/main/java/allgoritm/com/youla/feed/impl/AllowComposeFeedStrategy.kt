package allgoritm.com.youla.feed.impl

import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class AllowComposeFeedStrategy(
        private val initVisibility: Boolean,
        private val numUpdatesWhenNotVisible: Int
) {

    private var visibility = AtomicBoolean(initVisibility)
    private var numberUpdates = AtomicInteger(numUpdatesWhenNotVisible)

    @Synchronized
    fun setVisible(isVisible: Boolean) {
        if (isVisible) {
            numberUpdates.set(numUpdatesWhenNotVisible)
        }
        visibility.set(isVisible)
    }

    @Synchronized
    fun allowCompose() : Boolean {
        return if (visibility.get()) {
            true
        } else {
            numberUpdates.getAndDecrement() > 0
        }
    }

    fun resetBackground() {
        numberUpdates.set(numUpdatesWhenNotVisible)
    }
}