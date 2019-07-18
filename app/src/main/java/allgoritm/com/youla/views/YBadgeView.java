package allgoritm.com.youla.views;

import allgoritm.com.youla.admob.demo.R;
import allgoritm.com.youla.models.entity.Badge;
import allgoritm.com.youla.utils.ScreenUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

/**
 * Created by next on 16.03.16.
 */
public class YBadgeView extends FrameLayout {


    /**
     * Типы бэйджей
     * Могут быть использованны для настройки различных параметров бэйджа
     * */
    private enum BADGE_TYPE{
        INFO, DISTANCE;
    }

    TextView textView;
    ImageView deliveryImageView;

    public YBadgeView(Context context) {
        super(context);
        prepare(context);
    }

    public YBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        prepare(context);
    }

    public YBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        prepare(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public YBadgeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        prepare(context);
    }

    private void prepare(Context context) {
        View view = inflate(context, R.layout.badge_view, this);
        textView = view.findViewById(R.id.textView);
        deliveryImageView = view.findViewById(R.id.badgeDeliveryImageView);
    }

    public void setupBadge(Badge b) {
        if (b.isEmpty()) {
            textView.setVisibility(GONE);
            deliveryImageView.setVisibility(GONE);
            return;
        }

        textView.setVisibility(VISIBLE);

        textView.setText(b.getTitle());

        GradientDrawable background = new GradientDrawable();
        background.setCornerRadius(ScreenUtils.dpToPx(2));
        background.setColor(Color.parseColor(b.getBackgroundColor()));
        textView.setBackground(background);

        textView.setTextColor(Color.parseColor(b.getTextColor()));

        setContentDescription(b.getContentDescription());

        if (b.getShowDistance()) {
            changeBadgeFormat(BADGE_TYPE.DISTANCE);
        } else {
            changeBadgeFormat(BADGE_TYPE.INFO);
        }

        if (b.getShowDelivery()) {
            deliveryImageView.setVisibility(VISIBLE);
            textView.setPadding(ScreenUtils.dpToPx(30), textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
        } else {
            textView.setPadding(ScreenUtils.dpToPx(6), textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
            deliveryImageView.setVisibility(GONE);
        }
    }

    public void setupBadge(int status, int blockMode, int soldMode, int archiveMode) {
        setupBadge(status, blockMode, soldMode, archiveMode, false, null, false);
    }

    public void setupBadge(int status,
                           int blockMode,
                           int soldMode,
                           int archiveMode,
                           boolean showExpiring,
                           @Nullable String distanceText,
                           boolean showDistance) {
        setupBadge(status, blockMode, soldMode, archiveMode, showExpiring, distanceText, showDistance, false);

    }

    public void setupBadge(int status,
                           int blockMode,
                           int soldMode,
                           int archiveMode,
                           boolean showExpiring,
                           @Nullable String distanceText,
                           boolean showDistance,
                           boolean showDeliveryIcon) {

        textView.setVisibility(VISIBLE);
        textView.setPadding(ScreenUtils.dpToPx(8), textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
        deliveryImageView.setVisibility(GONE);
        changeBadgeFormat(BADGE_TYPE.INFO);
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(textView.getText()) || textView.getVisibility() != VISIBLE;
    }

    public void setupCustomBadeOrHideIfNoText(String badgeText, @DrawableRes int bkgId){
        if(TextUtils.isEmpty(badgeText)){
            textView.setVisibility(GONE);
            deliveryImageView.setVisibility(GONE);
        }else {
            textView.setVisibility(VISIBLE);
            textView.setPadding(ScreenUtils.dpToPx(6), textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
            deliveryImageView.setVisibility(GONE);
            changeBadgeFormat(BADGE_TYPE.INFO);
            textView.setText(badgeText);
            textView.setBackgroundResource(bkgId);
            textView.setTextColor(Color.BLACK);
            setContentDescription(badgeText);
        }
    }

    /**
     * Устанавливает параметры textView необходимые для бэйжда
     *
     * @param text текст который должен отображаться на бэйдже
     * */
    private void setupDistanceBadge(String text, boolean showDeliveryIcon) {
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(GONE);
        }
        else {
            changeBadgeFormat(BADGE_TYPE.DISTANCE);
            textView.setVisibility(VISIBLE);
            textView.setText(text);
            textView.setTextColor(Color.WHITE);
            if(showDeliveryIcon) {
                deliveryImageView.setVisibility(VISIBLE);
                textView.setPadding(ScreenUtils.dpToPx(30), textView.getPaddingTop(), textView.getPaddingRight(), textView.getPaddingBottom());
            }
        }
    }


    /**
     * Изменяет параметры textView взависимости от типа бэйджа {@link BADGE_TYPE}
     *
     * @param type тип бэйджа
     * */
    private void changeBadgeFormat(BADGE_TYPE type){
        switch (type){
            case INFO :
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textView.setLetterSpacing(0.2f);
                    textView.setTextSize(10);
                }
                textView.setAllCaps(true);
            break;
            case DISTANCE:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textView.setLetterSpacing(0);
                    textView.setTextSize(13);
                }
                textView.setAllCaps(false);
                break;
        }

    }

    public void hideBadge() {
        textView.setVisibility(GONE);
    }
}
