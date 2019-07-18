package allgoritm.com.youla.image;

import allgoritm.com.youla.utils.ScreenUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Debug;
import androidx.core.graphics.drawable.DrawableCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by IvanLozenko on 03.10.15.
 *
 */
public class ImageTools {

    public static final String PNG_EXT = "png";
    public static final int BITMAP_COMPRESS_QUALITY = 100;
    public static final String JPG_EXT = "jpg" ;
    private static final String ORIG_URL_PART =  "images/orig";
    public static int WIDTH = -1;
    public static TreeSet<Integer> supportedSizes = getSupportedSizes();
    public static TreeMap<Integer, Integer> supportedRectangleSizes = getRectSupportedSizes();

    private static TreeMap<Integer, Integer> getRectSupportedSizes() {
        TreeMap<Integer, Integer> sizes = new TreeMap<>();
        sizes.put(1600, 710);
        sizes.put(1440, 640);
        sizes.put(1080, 480);
        sizes.put(720, 320);
        sizes.put(540, 240);
        sizes.put(360, 160);
        return sizes;

    }


    public static void normaliseRotation(File imageFile){
        try {
            ExifInterface ei = new ExifInterface(imageFile.getPath());
            Matrix matrix = new Matrix();
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            if (orientation != ExifInterface.ORIENTATION_NORMAL && orientation != ExifInterface.ORIENTATION_UNDEFINED){
                switch (orientation){
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;
                }
            }

            Bitmap bitmap = getBitmapFromFile(imageFile.getPath());
            Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            rotated.compress(Bitmap.CompressFormat.JPEG, BITMAP_COMPRESS_QUALITY, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            bitmap.recycle();
            rotated.recycle();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getResizeImageUrl(String inUrl, int realWidth, int realHeight){
        String outUrl = inUrl;
        if (inUrl.contains(ORIG_URL_PART)){
            int maxRealSize = Math.max(realWidth, realHeight);
            Integer size = supportedSizes.ceiling(maxRealSize);
            if (size != null && maxRealSize > 0) {
                outUrl = outUrl.replace(ORIG_URL_PART, "images/" + size + "_" + size);
            }
        }
        return outUrl;
    }

    public static String getResizedRectangleImageUrl(Context context, String inUrl){
        String outUrl = inUrl;
        if (inUrl.contains(ORIG_URL_PART)){
            int screenWidth = width(context);
            Integer w = supportedRectangleSizes.ceilingKey(screenWidth);
            if(w != null){
                int h = supportedRectangleSizes.get(w);
                outUrl = outUrl.replace(ORIG_URL_PART, "images/" + w + "_" + h);
            }
        }
        return outUrl;
    }

    public static Bitmap getBitmapFromFile(String path) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        double inSampleSize;

        final double curMemory = Debug.getNativeHeapSize();
        final double reqMemory = width * height * getBytesPerPixel(options.inPreferredConfig);

        inSampleSize =  Math.ceil(reqMemory/curMemory);
        return (int)inSampleSize;
    }

    private static int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    public static Drawable getTintedIcon(Drawable drawable, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    /*
    * Размеры изображений.
    * https://allgoritm.slack.com/archives/youla-develop/p1461251848000277
    * */
    private static TreeSet<Integer> getSupportedSizes() {
        TreeSet<Integer> result = new TreeSet<>();
        result.add(80);
        result.add(160);
        result.add(240);
        result.add(360);
        result.add(540);
        result.add(780);
        result.add(1284);
        result.add(1440);
        return result;
    }

    private static String md5(final String s) {
        try {
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException ignore) {}
        return "";
    }

    private static synchronized int width(Context context){
        if(WIDTH == -1){
            WIDTH = ScreenUtils.getScreenWidthInPx(context);
        }
        return WIDTH;
    }

    public static String getUniqPhotoFileName() {
        return String.format("photo%d.jpg", System.currentTimeMillis());
    }

    public static String getUniqPhotoFileName(Object image) {
        return String.format("photo%d.jpg", image.hashCode()+System.currentTimeMillis());
    }

    public static String getPhotoFileNameById(String imageName) {
        return String.format("photo%s.jpg", imageName);
    }
}
