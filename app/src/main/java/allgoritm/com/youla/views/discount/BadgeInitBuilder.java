package allgoritm.com.youla.views.discount;

import androidx.annotation.NonNull;

class BadgeInitBuilder {
    private static final int DEFAULT_INT = -99;
    private CharSequence discountText = null;
    private int sideSize = DEFAULT_INT;
    private CardPosition cardPosition = CardPosition.TOP_RIGHT;
    private int cornerRadiusPx = DEFAULT_INT;
    private int startGradientColor = DEFAULT_INT;
    private int endGradientColor = DEFAULT_INT;
    private int textSizePx = DEFAULT_INT;
    private int textColor = DEFAULT_INT;
    private int shadowColor = DEFAULT_INT;
    private int backgroundColor = DEFAULT_INT;

    public BadgeInitBuilder setText(@NonNull CharSequence text) {
        this.discountText = text;
        return this;
    }

    public BadgeInitBuilder setBadgeSide(int sideSizePx) {
        sideSize = sideSizePx;
        return this;
    }

    public BadgeInitBuilder setPosition(@NonNull CardPosition position) {
        cardPosition = position;
        return this;
    }

    public BadgeInitBuilder setCornerRadius(int cornerRadiusPx) {
        this.cornerRadiusPx = cornerRadiusPx;
        return this;
    }

    public BadgeInitBuilder setStartColor(int startGradientColor) {
        this.startGradientColor = startGradientColor;
        return this;
    }

    public BadgeInitBuilder setEndColor(int endGradientColor) {
        this.endGradientColor = endGradientColor;
        return this;
    }

    public BadgeInitBuilder setTextSize(int textSizePx) {
        this.textSizePx = textSizePx;
        return this;
    }

    public BadgeInitBuilder setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public BadgeInitBuilder setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public BadgeInitBuilder setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public boolean hasDiscountText() {
        return discountText != null;
    }

    public boolean hasSideSize() {
        return DEFAULT_INT != sideSize;
    }

    public boolean hasCornerRadius() {
        return DEFAULT_INT != cornerRadiusPx;
    }

    public boolean hasStartColor() {
        return DEFAULT_INT != startGradientColor;
    }

    public boolean hasEndColor() {
        return DEFAULT_INT != endGradientColor;
    }

    public boolean hasTextSize() {
        return DEFAULT_INT != textSizePx;
    }

    public boolean hasTextColor() {
        return DEFAULT_INT != textColor;
    }

    public boolean hasShadowColor() {
        return DEFAULT_INT != shadowColor;
    }

    public boolean hasBackgroundColor() {
        return DEFAULT_INT != backgroundColor;
    }

    public CharSequence getDiscountText() {
        return discountText;
    }

    public int getBadgeSide() {
        return sideSize;
    }

    public CardPosition getCardPosition() {
        return cardPosition;
    }

    public int getCornerRadiusPx() {
        return cornerRadiusPx;
    }

    public int getStartColor() {
        return startGradientColor;
    }

    public int getEndColor() {
        return endGradientColor;
    }

    public int getTextSizePx() {
        return textSizePx;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }


}
