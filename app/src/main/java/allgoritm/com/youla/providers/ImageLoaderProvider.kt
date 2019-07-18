package allgoritm.com.youla.providers

import android.widget.ImageView

interface ImageLoaderProvider {

    fun loadImageAllCorners(view: ImageView, url: String?)

    fun loadImageTopCorners(view: ImageView, url: String?)

    fun loadImageSmallTopCorners(view: ImageView, url: String?)

    fun loadImageRounded(view: ImageView, url: String?)

    fun loadImage(view: ImageView, url: String?, placeHolder: Int? = null)
}