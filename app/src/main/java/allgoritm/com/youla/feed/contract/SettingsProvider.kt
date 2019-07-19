package allgoritm.com.youla.feed.contract

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsProvider @Inject constructor() {

    fun getFeedComposePageSize() : Int = getFeedPageSize()

    fun getFeedPageSize(): Int = 30

    fun getFeedPageThreshold(): Int = 15

}