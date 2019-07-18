package allgoritm.com.youla.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

import androidx.core.content.res.ResourcesCompat
import java.util.Locale

class ResourceProvider(private val context: Context) {

    fun getString(@StringRes id: Int): String = context.getString(id)

    fun getString(@StringRes id: Int, vararg args: Any): String = context.getString(id, *args)

    fun getQuantityString(@PluralsRes id: Int, quantity: Int): String {
        return context.resources.getQuantityString(id, quantity)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? = ResourcesCompat.getDrawable(context.resources, id, context.theme)

    fun getDimensionPixelSize(@DimenRes id: Int): Int = context.resources.getDimensionPixelSize(id)

    fun getDimension(@DimenRes id: Int): Float = context.resources.getDimension(id)

    fun getColor(@ColorRes id: Int) = ResourcesCompat.getColor(context.resources, id, context.theme)

    fun getColorStr(@ColorRes id: Int): String {
        return "#${Integer.toHexString(getColor(id))}"
    }

    fun getSmallestScreeWidthPx(): Int {
        return (context.resources.configuration.smallestScreenWidthDp * context.resources.displayMetrics.density).toInt()
    }

    fun getSmallestScreeWidthDp(): Int {
        return context.resources.configuration.smallestScreenWidthDp
    }

    @Suppress("DEPRECATION")
    fun getLocale(): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return context.resources.configuration.locales.get(0)
        } else {
            return context.resources.configuration.locale
        }
    }

}