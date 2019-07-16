package allgoritm.com.youla.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IvanLozenko on 03.10.15.
 */
public class ScreenUtils {

    public static int getScreenWidthInDp(DisplayMetrics displayMetrics) {
        return (int)(displayMetrics.widthPixels / displayMetrics.density);
    }

    public static int getScreenWidthInPx(DisplayMetrics displayMetrics) {
        return displayMetrics.widthPixels;
    }

    public static int getScreenWidthInDp(@NotNull Context context) {
        return getScreenWidthInDp(context.getResources());
    }

    public static int getScreenWidthInDp(@NotNull Resources resources) {
        return getScreenWidthInDp(resources.getDisplayMetrics());
    }

    public static int getScreenWidthInPx(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return getScreenWidthInPx(displayMetrics);
    }

    public static int getScreenHeightInDp(DisplayMetrics displayMetrics) {
        return (int)(displayMetrics.heightPixels / displayMetrics.density);
    }

    public static int getScreenHeightInDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return getScreenHeightInDp(displayMetrics);
    }

    public static int getScreenHeightInPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static float dpToPxFloat(float dp)
    {
        return dp * Resources.getSystem().getDisplayMetrics().density;
    }

    public static float pxToDpFloat(float px)
    {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int spToPx(float sp)
    {
        return (int) (sp * Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


    public static int getScreenWidthInPixels(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
	}

    public static int pxToSp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().scaledDensity);
    }
}
