package allgoritm.com.youla.feed.contract

import allgoritm.com.youla.utils.ResourceProvider
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsProvider @Inject constructor(
    private val sp: SharedPreferences,
    private val rp: ResourceProvider
) {

    fun getFeedComposePageSize() : Int = getFeedPageSize()

    fun getFeedPageSize(): Int = 30

    fun getFeedPageThreshold(): Int = 15

}