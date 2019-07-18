package allgoritm.com.youla.feed.contract

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsProvider @Inject constructor() {

    val chatPageSize = 20
    val chatThreshold = 5
    val chatsPageSize = 20
    val chatsThreshold = 5

    fun getFeedComposePageSize() : Int = getFeedPageSize()

    fun getFeedPageSize(): Int = 30

    fun getFeedPageThreshold(): Int = 15

    fun getCarouselPageSize(): Int = 60

    fun getCarouselThreshold() = 30

    fun getSubscriptionsGroupPageSize() = 10

    fun getSubscriptionsGroupThreshold() = 5

}