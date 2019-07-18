package allgoritm.com.youla.utils.ktx

import android.graphics.Bitmap

/**
 * Bitmap может возвращать null в getConfig() и при вызове Bitmap.createBitmap() с передачей
 * Config в качестве параметра, будет выброшено исключение NullPointerException.
 * ARGB_8888 используется в качестве лучшей альтернативы
 */
fun Bitmap.getNonNullConfig(): Bitmap.Config = config ?: Bitmap.Config.ARGB_8888