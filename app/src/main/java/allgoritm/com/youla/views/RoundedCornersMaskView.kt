package allgoritm.com.youla.views

import allgoritm.com.youla.admob.demo.R
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View


/**
 * Created by next on 12.10.2018.
 */
class RoundedCornersMaskView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint
    private val path: Path
    private val rect: RectF

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.RoundedCornersMaskView)
        val color = ta.getColor(R.styleable.RoundedCornersMaskView_cornersColor, Color.WHITE)
        ta.recycle()
        paint = Paint(ANTI_ALIAS_FLAG).apply {
            this.color = color
        }
        setLayerType(View.LAYER_TYPE_SOFTWARE, paint)
        path = Path()
        rect = RectF()
        rect.left = 0f
        rect.top = 0f
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        path.moveTo(0f, 0f)
        path.lineTo(width/2, 0f)
        rect.right = width
        rect.bottom = height
        path.arcTo(rect,
                -90f,
                -359f
        )
        path.lineTo(width, 0f)
        path.lineTo(width, height)
        path.lineTo(0f, height)
        path.lineTo(0f, 0f)
        path.close()
        canvas.drawPath(path, paint)
        path.reset()
    }

}