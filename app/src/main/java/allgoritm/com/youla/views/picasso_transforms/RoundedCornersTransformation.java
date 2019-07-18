package allgoritm.com.youla.views.picasso_transforms;

import android.graphics.*;
import com.squareup.picasso.Transformation;

public class RoundedCornersTransformation implements Transformation {

    public static final class Builder {

        private int radius;
        private int margin;
        private boolean topLeftCorner;
        private boolean topRightCorner;
        private boolean bottomLeftCorner;
        private boolean bottomRightCorner;


        public Builder() {
        }

        public Builder withRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder withMargin(int margin) {
            this.margin = margin;
            return this;
        }

        public Builder withTopLeftCorner() {
            this.topLeftCorner = true;
            return this;
        }

        public Builder withTopRightCorner() {
            this.topRightCorner = true;
            return this;
        }

        public Builder withBottomLeftCorner() {
            this.bottomLeftCorner = true;
            return this;
        }

        public Builder withBottomRightCorner() {
            this.bottomRightCorner = true;
            return this;
        }

        public Builder roundTopLeftCorner(boolean round) {
            this.topLeftCorner = round;
            return this;
        }

        public Builder roundTopRightCorner(boolean round) {
            this.topRightCorner = round;
            return this;
        }

        public Builder roundBottomLeftCorner(boolean round) {
            this.bottomLeftCorner = round;
            return this;
        }

        public Builder roundBottomRightCorner(boolean round) {
            this.bottomRightCorner = round;
            return this;
        }

        public RoundedCornersTransformation build() {
            return new RoundedCornersTransformation(radius, margin, topLeftCorner, topRightCorner, bottomLeftCorner, bottomRightCorner);
        }
    }

    private final int radius;
    private final int margin;
    private String KEY = "";
    private boolean topLeftCorner = false;
    private boolean topRightCorner = false;
    private boolean bottomLeftCorner = false;
    private boolean bottomRightCorner = false;

    /**
     * Creates rounded transformation for all corners.
     *
     * @param radius radius is corner radius in px
     * @param margin margin is the board in px
     */
    private RoundedCornersTransformation(final int radius, final int margin) {
        this.radius = radius;
        this.margin = margin;
        if (KEY.isEmpty()) KEY = "rounded_" + radius + margin;
    }


    /**
     * Creates rounded transformation for top or bottom corners.
     *
     * @param radius radius is corner radius in px
     * @param margin margin is the board in px
     */
    private RoundedCornersTransformation(final int radius, final int margin, boolean topLeftCorner, boolean topRightCorner, boolean bottomLeftCorner, boolean bottomRightCorner) {
        this(radius, margin);
        this.topLeftCorner = topLeftCorner;
        this.topRightCorner = topRightCorner;
        this.bottomLeftCorner = bottomLeftCorner;
        this.bottomRightCorner = bottomRightCorner;
        KEY = "rounded_" + radius + margin + topLeftCorner + topRightCorner + bottomLeftCorner + bottomRightCorner;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        if(topLeftCorner && topRightCorner && bottomLeftCorner && bottomRightCorner) {
            // Uses native method to draw symmetric rounded corners
            canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin,
                    source.getHeight() - margin), radius, radius, paint);
        } else {
            // Uses custom path to generate rounded corner individually
            canvas.drawPath(RoundedRect(margin, margin, source.getWidth() - margin,
                    source.getHeight() - margin, radius, radius, topLeftCorner, topRightCorner,
                    bottomRightCorner, bottomLeftCorner), paint);
        }


        if (source != output) {
            source.recycle();
        }

        return output;
    }


    @Override
    public String key() {
        return KEY;
    }

    /**
     * Prepares a path for rounded corner selectively.
     * @param leftX The X coordinate of the left side of the rectangle
     * @param topY The Y coordinate of the top of the rectangle
     * @param rightX The X coordinate of the right side of the rectangle
     * @param bottomY The Y coordinate of the bottom of the rectangle
     * @param rx The x-radius of the oval used to round the corners
     * @param ry The y-radius of the oval used to round the corners
     * @param topLeft
     * @param topRight
     * @param bottomRight
     * @param bottomLeft
     * @return
     */
    private static Path RoundedRect(float leftX, float topY, float rightX, float bottomY, float rx,
                                   float ry, boolean topLeft, boolean topRight, boolean
                                           bottomRight, boolean bottomLeft) {
        Path path = new Path();
        if (rx < 0) rx = 0;
        if (ry < 0) ry = 0;
        float width = rightX - leftX;
        float height = bottomY - topY;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(rightX, topY + ry);
        if (topRight)
            path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        else{
            path.rLineTo(0, -ry);
            path.rLineTo(-rx,0);
        }
        path.rLineTo(-widthMinusCorners, 0);
        if (topLeft)
            path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        else{
            path.rLineTo(-rx, 0);
            path.rLineTo(0,ry);
        }
        path.rLineTo(0, heightMinusCorners);

        if (bottomLeft)
            path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        else{
            path.rLineTo(0, ry);
            path.rLineTo(rx,0);
        }

        path.rLineTo(widthMinusCorners, 0);
        if (bottomRight)
            path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
        else{
            path.rLineTo(rx,0);
            path.rLineTo(0, -ry);
        }

        path.rLineTo(0, -heightMinusCorners);

        path.close();

        return path;
    }
}
