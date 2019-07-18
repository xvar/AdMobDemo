package allgoritm.com.youla.loader

import allgoritm.com.youla.admob.demo.R
import allgoritm.com.youla.image.ImageTools
import allgoritm.com.youla.providers.ImageLoaderProvider
import allgoritm.com.youla.utils.ScreenUtils
import allgoritm.com.youla.views.picasso_transforms.CircleTransform
import allgoritm.com.youla.views.picasso_transforms.RoundedCornersTransformation
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.squareup.picasso.Transformation
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class ImageLoader @JvmOverloads constructor(context: Context, private val picasso: Picasso = Picasso.with(context)) : ImageLoaderProvider {

    private val NO_VALUE = -1
    private val screenMinSide = Math.min(
            ScreenUtils.getScreenWidthInPixels(context),
            ScreenUtils.getScreenHeightInPixels(context))
    private val defaultRadius = ScreenUtils.dpToPx(8)
    private val leftCornersTransform: Transformation by lazy {
        RoundedCornersTransformation.Builder()
                .withRadius(defaultRadius)
                .withTopLeftCorner()
                .withBottomLeftCorner()
                .build()
    }
    private val rightCornersTransform: Transformation by lazy {
        RoundedCornersTransformation.Builder()
                .withRadius(defaultRadius)
                .withTopRightCorner()
                .withBottomRightCorner()
                .build()
    }
    private val topCornersTransform: Transformation by lazy {
        RoundedCornersTransformation.Builder()
                .withRadius(defaultRadius)
                .withTopLeftCorner()
                .withTopRightCorner()
                .build()
    }

    private val topSmallCornersTransform: Transformation by lazy {
        RoundedCornersTransformation.Builder()
                .withRadius(ScreenUtils.dpToPx(4))
                .withTopLeftCorner()
                .withTopRightCorner()
                .build()
    }

    private val allCornersTransform: Transformation by lazy {
        RoundedCornersTransformation.Builder()
                .withRadius(defaultRadius)
                .withTopLeftCorner()
                .withTopRightCorner()
                .withBottomRightCorner()
                .withBottomLeftCorner()
                .build()
    }

    private val roundedTransform: Transformation by lazy {
        CircleTransform()
    }

    private val roundedWhiteBorderTransform: Transformation by lazy {
        CircleTransform(ContextCompat.getColor(context, R.color.white), ScreenUtils.dpToPx(2))
    }

    @JvmOverloads
    fun loadImageFromURL(url: String?, width: Int = NO_VALUE, height: Int = NO_VALUE, transform: Transformation? = null): Single<LinkedList<Bitmap?>> {
        return Single.fromCallable<LinkedList<Bitmap?>> { load(listOf(url), width, height, transform) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadImageRounded(view: ImageView, url: String?) {
        loadImage(view, url, R.drawable.oval_placeholder, roundedTransform)
    }

    override fun loadImageAllCorners(view: ImageView, url: String?) {
        loadImage(view, url, R.drawable.corners_8_placeholder, allCornersTransform)
    }

    override fun loadImageTopCorners(view: ImageView, url: String?) {
        loadImage(view, url, R.drawable.top_corners_8_placeholder, topCornersTransform)
    }

    override fun loadImageSmallTopCorners(view: ImageView, url: String?) {
        loadImage(view, url, R.drawable.top_corners_4_placeholder, topSmallCornersTransform)
    }

    override fun loadImage(view: ImageView, url: String?, placeHolder: Int?) {
        loadImage(view, url, placeHolder, null, true)
    }


    fun loadImage(view: ImageView,
                  url: String?, placeHolder: Int? = null,
                  transform: Transformation? = null, crop: Boolean = true,
                  forceOrig: Boolean = false, drawablePlaceholder: Drawable? = null) {
        if (!url.isNullOrBlank()) {
            view.doOnPreDraw {
                val resizedUrl = if (forceOrig) url else ImageTools.getResizeImageUrl(url, it.width, it.height)
                picasso.load(resizedUrl).proceed(placeHolder, transform, crop, drawablePlaceholder, view)
            }
        } else {
            placeHolder?.let { view.setImageResource(it) }
        }
    }

    fun loadImage(
            view: ImageView,
            url: String?,
            placeHolder: Drawable,
            transform: Transformation
    ) {
        val isNullOrBlank = url.isNullOrBlank()
        if (isNullOrBlank) {
            view.setImageDrawable(placeHolder)
        } else {
            view.doOnPreDraw {
                val resizedUrl = ImageTools.getResizeImageUrl(url, it.width, it.height)
                picasso
                        .load(resizedUrl)
                        .centerCrop()
                        .fit()
                        .placeholder(placeHolder)
                        .transform(transform)
                        .into(view)
            }
        }
    }

    fun loadImageOrResource(view: ImageView, url: String?, placeHolder: Int, transform: Transformation? = null) {
        if (!url.isNullOrBlank()) {
            loadImage(view, url, placeHolder, transform)
        } else {
            view.setImageResource(placeHolder)
        }
    }

    fun loadScreenOptimizedImage(url: String?): Single<LinkedList<Bitmap?>> = loadScreenOptimizedImage(listOf(url))

    fun loadScreenOptimizedImage(url: List<String?>): Single<LinkedList<Bitmap?>> {
        return Single.just(url)
                .map { it.map { if (it == null) null else ImageTools.getResizeImageUrl(it, screenMinSide, screenMinSide) } }
                .map { load(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun load(urlList: List<String?>, width: Int = NO_VALUE, height: Int = NO_VALUE, transform: Transformation? = null): LinkedList<Bitmap?> {
        val bitmaps = LinkedList<Bitmap?>()
        val tList = ArrayList<Transformation>()

        if (transform != null) {
            tList.add(transform)
        }

        urlList.forEach {

            val result = if (it.isNullOrEmpty()) null else picasso.load(it)
                    .apply { if (width != NO_VALUE && height != NO_VALUE) resize(width, height) }
                    .transform(tList).get()
            bitmaps.add(result)
        }

        return bitmaps
    }

    private fun RequestCreator.proceed(placeHolder: Int? = null,
                                       transform: Transformation? = null, crop: Boolean = true,
                                       drawablePlaceholder: Drawable? = null,
                                       view: ImageView) {
        placeHolder?.let { placeholder(it) }
        drawablePlaceholder?.let { placeholder(it) }
        transform?.let { transform(it) }
        if (crop) {
            fit()
            centerCrop()
        }
        into(view)
    }

}