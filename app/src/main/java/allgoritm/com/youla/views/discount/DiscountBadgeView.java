package allgoritm.com.youla.views.discount;

import allgoritm.com.youla.admob.demo.R;
import allgoritm.com.youla.utils.ScreenUtils;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;


public class DiscountBadgeView extends FrameLayout {
    private CharSequence discountText = "";
    private int maxSize;
    private CardPosition cardPosition = CardPosition.TOP_RIGHT;

    private TextPaint paint;
    private int cornerRadiusPx;
    private Paint textPaint;
    private Rect textBounds;
    private DiscountBadgeDrawController viewController;
    private int startGradientColor;
    private int endGradientColor;
    private int textSizePx;
    private int textColor;
    private int shadowColor;
    private int backgroundColor;
    private float measuredTextWidth;

    public DiscountBadgeView(Context context) {
        super(context);
        prepare(null, 0);
    }

    public DiscountBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        prepare(attrs, 0);
    }

    public DiscountBadgeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        prepare(attrs, defStyle);
    }

    private void prepare(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DiscountBadgeView, defStyle, R.style.DiscountBadgeView_Default);

        try {
            discountText = a.getText(R.styleable.DiscountBadgeView_android_text);
            startGradientColor = a.getColor(R.styleable.DiscountBadgeView_badgeStartColor, 0);
            endGradientColor = a.getColor(R.styleable.DiscountBadgeView_badgeEndColor, 0);
            cardPosition = CardPosition.from(a.getInt(R.styleable.DiscountBadgeView_badgePosition, CardPosition.TOP_RIGHT.getPosition()));
            textSizePx = a.getDimensionPixelSize(R.styleable.DiscountBadgeView_badgeTextSize, 0);
            maxSize = a.getDimensionPixelSize(R.styleable.DiscountBadgeView_badgeSide, maxSize);
            cornerRadiusPx = a.getDimensionPixelSize(R.styleable.DiscountBadgeView_cornerRadius, 0);
            textColor = a.getColor(R.styleable.DiscountBadgeView_badgeTextColor, 0);
            shadowColor = a.getColor(R.styleable.DiscountBadgeView_badgeShadowColor, 0);
            backgroundColor = a.getColor(R.styleable.DiscountBadgeView_badgeBackgroundColor, 0);
        } finally {
            a.recycle();
        }

//        initPaint(textColor, shadowColor, startGradientColor, endGradientColor, textSizePx);
        initPaint(textColor, backgroundColor, textSizePx);
    }

    private void initPaint(int textColor, int backgroundColor, int textSizePx) {
        viewController = new DiscountBadgeDrawController(cardPosition);

        paint = new TextPaint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(backgroundColor);
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
        setWillNotDraw(false);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSizePx);
        Typeface tfm = Typeface.create("sans-serif", Typeface.BOLD);
        setLayerType(LAYER_TYPE_SOFTWARE, textPaint);
        textPaint.setTypeface(tfm);
        textPaint.setTextAlign(Paint.Align.CENTER);

        textBounds = new Rect();
        textPaint.getTextBounds(discountText.toString(), 0, discountText.length(), textBounds);
        measuredTextWidth = textPaint.measureText(discountText.toString());
    }

    private void initPaint(int textColor, int shadowColor, int startGradientColor, int endGradientColor, int textSizePx) {
        viewController = new DiscountBadgeDrawController(cardPosition);

        paint = new TextPaint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
        setWillNotDraw(false);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSizePx);
        Typeface tfm = Typeface.create("sans-serif", Typeface.BOLD);
        setLayerType(LAYER_TYPE_SOFTWARE, textPaint);
        textPaint.setShadowLayer(ScreenUtils.dpToPx(1), 0f, ScreenUtils.dpToPx(1), shadowColor);
        textPaint.setTypeface(tfm);
        textPaint.setTextAlign(Paint.Align.CENTER);

        textBounds = new Rect();
        textPaint.getTextBounds(discountText.toString(), 0, discountText.length(), textBounds);
        measuredTextWidth = textPaint.measureText(discountText.toString());

        Shader shader = viewController.getGradient(maxSize, startGradientColor, endGradientColor);
        Matrix gradientRotateMatrix = new Matrix();
        gradientRotateMatrix.setRotate(viewController.getGradientRotationAngle());
        shader.setLocalMatrix(gradientRotateMatrix);
        paint.setShader(shader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = getSide(widthMode, widthSize, maxSize);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = getSide(heightMode, heightSize, maxSize);

        int side = width < height ? width : height;
        setMeasuredDimension(side, side);
    }

    private int getSide(int measureSpecMode, int measureSpecSize, int desired) {
        int side;
        if (measureSpecMode == MeasureSpec.EXACTLY) {
            side = measureSpecSize;
        } else {
            if (measureSpecMode == MeasureSpec.AT_MOST) {
                side = Math.min(desired, measureSpecSize);
            } else {
                side = desired;
            }
        }
        return side;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDiscount(canvas);
        super.onDraw(canvas);
    }

    private void drawDiscount(Canvas canvas) {
        int canvasHeight = canvas.getHeight();
        int canvasWidth = canvas.getWidth();

        canvas.drawPath(viewController.getTrianglePath(canvasWidth, canvasHeight, cornerRadiusPx), paint);

        canvas.save();
        canvas.rotate(
                viewController.getTextRotationAngle(),
                viewController.getRotationXPoint(canvasWidth),
                0
        );

        canvas.drawText(
                discountText.toString(),
                viewController.getXForText(canvasWidth, textBounds, measuredTextWidth),
                viewController.getYForText(canvasHeight, textBounds),
                textPaint
        );

        canvas.restore();
    }

    public void setText(@NonNull CharSequence badgeText) {
        discountText = badgeText;
        invalidate();
    }

    void initWithBuilder(BadgeInitBuilder builder) {
        this.textSizePx = builder.hasTextSize() ? builder.getTextSizePx() : textSizePx;
        this.discountText = builder.hasDiscountText() ? builder.getDiscountText() : discountText;
        this.cornerRadiusPx = builder.hasCornerRadius() ? builder.getCornerRadiusPx() : cornerRadiusPx;
        this.maxSize = builder.hasSideSize() ? builder.getBadgeSide() : maxSize;
        this.startGradientColor = builder.hasStartColor() ? builder.getStartColor() : startGradientColor;
        this.endGradientColor = builder.hasEndColor() ? builder.getEndColor() : endGradientColor;
        this.cardPosition = builder.getCardPosition();
        this.textColor = builder.hasTextColor() ? builder.getTextColor() : textColor;
        this.shadowColor = builder.hasShadowColor() ? builder.getShadowColor() : shadowColor;
        this.backgroundColor = builder.hasBackgroundColor() ? builder.getBackgroundColor() : backgroundColor;

//        initPaint(textColor, shadowColor, startGradientColor, endGradientColor, textSizePx);
        initPaint(textColor, backgroundColor, textSizePx);
        forceLayout();
    }

}
