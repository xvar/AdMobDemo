package allgoritm.com.youla.views.discount;

import allgoritm.com.youla.utils.ScreenUtils;
import android.graphics.*;
import androidx.annotation.NonNull;

class DiscountBadgeDrawController {

    @NonNull
    private final CardPosition cardPosition;

    DiscountBadgeDrawController(@NonNull CardPosition cardPosition) {
        this.cardPosition = cardPosition;
    }

    LinearGradient getGradient(int sideSize, int startColor, int endColor) {
        if (cardPosition == CardPosition.TOP_RIGHT || cardPosition == CardPosition.BOTTOM_LEFT) {
            return new LinearGradient(0, 0, sideSize * 0.7f, 0, startColor, endColor, Shader.TileMode.MIRROR);
        }
        if (cardPosition == CardPosition.TOP_LEFT || cardPosition == CardPosition.BOTTOM_RIGHT) {
            return new LinearGradient(0, 0, sideSize * 0.7f, 0, endColor, startColor, Shader.TileMode.MIRROR);
        }

        throw new IllegalStateException("unknown card position");
    }

    float getGradientRotationAngle() {
        switch (cardPosition) {
            case TOP_LEFT:
            case BOTTOM_RIGHT:
                return 45;
            case BOTTOM_LEFT:
            case TOP_RIGHT:
                return 135;
        }
        throw new IllegalStateException("unknown card position");
    }

    Path getTrianglePath(float canvasWidth, float canvasHeight, float cornerRadius) {
        switch (cardPosition) {
            case TOP_LEFT:
                return getTopLeft(canvasWidth, canvasHeight, cornerRadius);
            case TOP_RIGHT:
                return getTopRight(canvasWidth, canvasHeight, cornerRadius);
            case BOTTOM_LEFT:
                return getBottomLeft(canvasWidth, canvasHeight, cornerRadius);
            case BOTTOM_RIGHT:
                return getBottomRight(canvasWidth, canvasHeight, cornerRadius);
        }
        throw new IllegalStateException("unknown card position");
    }

    private static Path getTopLeft(float canvasWidth, float canvasHeight, float cornerRadius) {
        Path path = new Path();
        path.moveTo(canvasWidth, 0);
        path.lineTo(cornerRadius, 0);
        path.arcTo(new RectF(
                        0,
                        0,
                        2 * cornerRadius,
                        2 * cornerRadius),
                -90,
                -90
        );
        path.lineTo(0, canvasHeight);
        path.lineTo(canvasWidth, 0);
        path.close();
        return path;
    }

    private static Path getTopRight(float canvasWidth, float canvasHeight, float cornerRadius) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(canvasWidth - cornerRadius, 0);
        path.arcTo(new RectF(
                        canvasWidth  - 2 * cornerRadius,
                        0,
                        canvasWidth,
                        2 * cornerRadius),
                -90,
                90
        );
        path.lineTo(canvasWidth, canvasHeight);
        path.lineTo(0, 0);
        path.close();
        return path;
    }

    private static Path getBottomLeft(float canvasWidth, float canvasHeight, float cornerRadius) {
        Path path = new Path();
        path.moveTo(0,0);
        path.lineTo(0, canvasHeight - cornerRadius);
        path.arcTo(new RectF(
                        0,
                        canvasHeight - 2 * cornerRadius,
                        2 * cornerRadius,
                        canvasHeight),
                -180,
                -90
        );
        path.lineTo(canvasWidth, canvasHeight);
        path.lineTo(0,0);
        path.close();
        return path;
    }

    private static Path getBottomRight(float canvasWidth, float canvasHeight, float cornerRadius) {
        Path path = new Path();
        path.moveTo(canvasWidth, 0);
        path.lineTo(canvasWidth, canvasHeight - cornerRadius);
        path.arcTo(new RectF(
                        canvasWidth - 2 * cornerRadius,
                        canvasHeight - 2 * cornerRadius,
                        canvasWidth,
                        canvasHeight),
                0,
                90
        );
        path.lineTo(0, canvasHeight);
        path.lineTo(canvasWidth, 0);
        path.close();
        return path;
    }

    float getRotationXPoint(int canvasWidth) {
        switch (cardPosition) {
            case TOP_LEFT:
                return canvasWidth;
            case TOP_RIGHT:
                return 0;
            case BOTTOM_LEFT:
                return 0;
            case BOTTOM_RIGHT:
                return canvasWidth;
        }
        throw new IllegalStateException("unknown card position");
    }

    float getTextRotationAngle() {
        switch (cardPosition) {
            case TOP_LEFT:
                return -45;
            case TOP_RIGHT:
                return 45;
            case BOTTOM_LEFT:
                return 45;
            case BOTTOM_RIGHT:
                return -45;
        }
        throw new IllegalStateException("unknown card position");
    }

    /**
     * Gives position for centering x-axis of the text
     * Android won't measure text bounds correctly, so
     * thanks Bane for the tip: https://chris.banes.me/2014/03/27/measuring-text/
     *
     * @param canvasWidth - width of canvas
     * @param textBounds - bounds of text
     * @param textWidth - measured width of text paint, cause textBounds are slightly restricted
     * @return center x position for text
     */
    int getXForText(int canvasWidth, Rect textBounds, float textWidth) {
        int centeredTextWidth = (int) (textBounds.centerX() - textWidth / 2);
        int xPos = (int) (1f * (Math.sqrt(2) * canvasWidth) / 2 - centeredTextWidth / 2);
        int xPosLeftRotate = (int) (canvasWidth - canvasWidth * Math.sqrt(2) / 2 - centeredTextWidth / 2);
        switch (cardPosition) {
            case TOP_LEFT:
                return xPosLeftRotate;
            case TOP_RIGHT:
                return xPos;
            case BOTTOM_LEFT:
                return xPos;
            case BOTTOM_RIGHT:
                return xPosLeftRotate;
        }
        throw new IllegalStateException("unknown card position");
    }

    int getYForText(int canvasHeight, @NonNull Rect textBounds) {
        float canvasHeightDp = ScreenUtils.pxToDpFloat(canvasHeight);
        float yPosDp = canvasHeightDp * 0.34f - 9.12f;
        int yPos = Math.round(ScreenUtils.dpToPxFloat(yPosDp));
        switch (cardPosition) {
            case TOP_LEFT:
                return -yPos;
            case TOP_RIGHT:
                return -yPos;
            case BOTTOM_LEFT:
                return yPos + textBounds.height();
            case BOTTOM_RIGHT:
                return yPos + textBounds.height();
        }
        throw new IllegalStateException("unknown card position");
    }
}
