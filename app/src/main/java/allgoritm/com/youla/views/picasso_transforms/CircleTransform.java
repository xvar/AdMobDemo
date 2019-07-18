package allgoritm.com.youla.views.picasso_transforms;

import allgoritm.com.youla.utils.ktx.BitmapExtKt;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.squareup.picasso.Transformation;

/**
 * Created by IvanLozenko on 13.08.15.
 */
public final class CircleTransform implements Transformation {

    private final int borderWidth;
    private final int borderColor;

    public CircleTransform() {
        borderColor = 0;
        borderWidth = 0;
    }

    public CircleTransform(int borderColor, int borderWidth) {
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size - borderWidth, size - borderWidth);
        if (squaredBitmap != source) {
            source.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, BitmapExtKt.getNonNullConfig(source));

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;

        if (borderWidth > 0) {
            Paint paintBg = new Paint();
            paintBg.setColor(borderColor);
            paintBg.setAntiAlias(true);
            canvas.drawCircle(r, r, r, paintBg);
        }

        canvas.drawCircle(r, r, r - borderWidth, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }

}