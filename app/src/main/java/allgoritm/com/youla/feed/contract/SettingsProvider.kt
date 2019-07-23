package allgoritm.com.youla.feed.contract

import allgoritm.com.youla.admob.demo.R
import allgoritm.com.youla.utils.ResourceProvider
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsProvider @Inject constructor(
    private val sp: SharedPreferences,
    private val rp: ResourceProvider
) {

    fun getFeedPageSize(): Int = 30

    fun getFeedPageThreshold(): Int = 15

    fun shouldUseAdvert() = sp.getBoolean(
        rp.getString(R.string.pref_key_use_advert),
        true
    )

    fun isLruSession() = sp.getBoolean(
        rp.getString(R.string.pref_key_use_lru_rotation_session),
        false
    )

    fun getStride(): Int {
        val savedStride = sp.getString(
            rp.getString(R.string.pref_key_stride),
            3.toString()
        )!!.toInt()
        return if (savedStride % 2 == 0) {
            savedStride - 1
        } else {
            savedStride
        }
    }

    val maxAdvertCount = 600
    val lruCacheSize = 10

}