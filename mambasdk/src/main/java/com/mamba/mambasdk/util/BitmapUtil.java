package com.mamba.mambasdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.view.Window;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <b>Description:</b> BitmapUtil provides the entry of interfaces to operate
 * bitmap. <br>
 * <b>Purpose:</b> Invoking interfaces in this class to do some operations on
 * bitmap.
 */
public class BitmapUtil {
    private static final String TAG = "BitmapUtil";

    private static String IMAGE_TYPT_BMP = "bmp";
    private static String IMAGE_TYPT_JPG = "jpg";
    private static String IMAGE_TYPT_JPEG = "jpeg";
    private static String IMAGE_TYPT_PNG = "png";
    private static String IMAGE_TYPT_GIF = "gif";

    private static double MAX_IMAGE_VALUE = 240.0;

    public static final int PICTURE_QUALITY_HIGH = 100;
    public static final int PICTURE_QUALITY_NORMAL = 60;

    private static long COMPRESS_IMAGE_FILE_SIZE_LIMIT = 250 * 1024;// 250k

    private static double COMPRESS_IMAGE_OVER_BIG_LIMIT = 2000.0;
    private static double COMPRESS_IMAGE_OVER_SMALL_LIMIT = 250.0;

    private static final int MAX_BITMAP_WIDTH = 720;
    private static final int MAX_BITMAP_HEIGHT = 1280;

    private static final float COMPRESS_PIC_SIZE = 7.2f;

    /**
     * Compress image or video file failed
     */
    public static final String COMPRESS_IMAGE_FAILED = "compress_image_failed";

    /**
     * Adjust the orientation degree of the bitmap if it has an orientation
     * rotate degree.
     * 
     * @param bitmap
     *            The bitmap to be adjusted.
     * @param soureFilePath
     *            The source file path of the bitmap.
     * @return The adjusted bitmap if the given bitmap has an orientation rotate
     *         degree. <br>
     *         The bitmap if it has no orientation rotate degree.
     */
    public static Bitmap adjustBitmapOrientation(Bitmap bitmap, String soureFilePath) {
        int degree = getOrientationRotateDegree(soureFilePath);
        if (0 == degree) {
            return bitmap;
        }

        return rotateBitmap(bitmap, degree);
    }

    /**
     * Get the orientation rotate degree of an image file.
     * 
     * @param soureFilePath
     *            The file path of the image.
     * @return The orientation rotate degree of an image file(90 or 180 or 270). <br>
     *         0, if the image file has no orientation rotate degree or the
     *         source file is not a valid image file.
     */
    public static int getOrientationRotateDegree(String soureFilePath) {
        if (null == soureFilePath) {
            return 0;
        }

        try {
            ExifInterface exif = new ExifInterface(soureFilePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90: {
                    return 90;
                }
                case ExifInterface.ORIENTATION_ROTATE_180: {
                    return 180;
                }
                case ExifInterface.ORIENTATION_ROTATE_270: {
                    return 270;
                }
                default: {
                    return 0;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Rotate a bitmap.
     * 
     * @param bitmap
     *            The bitmap to be rotated.
     * @param orientationDegree
     *            the orientation degree to be rotated.
     * @return The rotated degree. <br>
     *         The given bitmap, if the orientationDegree equals 0 or 360.
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientationDegree) {
        if (0 == orientationDegree || 360 == orientationDegree || null == bitmap) {
            return bitmap;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(orientationDegree);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);

        return rotatedBitmap;
    }

    private static boolean isNeedCompress(int srcWidth, int srcHeight, int screenWidth, int screenHight, long filesize) {
        int maxValue = srcWidth > srcHeight ? srcWidth : srcHeight;
        int minValue = srcWidth < srcHeight ? srcWidth : srcHeight;

        if (srcWidth <= screenWidth && srcHeight <= screenHight && filesize <= COMPRESS_IMAGE_FILE_SIZE_LIMIT) {
            // 图片二边小于屏幕两边，不需要压缩
            return false;
        }
        if (filesize <= COMPRESS_IMAGE_FILE_SIZE_LIMIT && maxValue <= COMPRESS_IMAGE_OVER_BIG_LIMIT) {
            // 文件小于规定大小 最大边小于规定
            return false;
        }

        if (filesize > COMPRESS_IMAGE_FILE_SIZE_LIMIT && (minValue <= COMPRESS_IMAGE_OVER_SMALL_LIMIT)
                && (maxValue <= COMPRESS_IMAGE_OVER_BIG_LIMIT)) {
            // 如果最小边小于规定并且最大边小于规定，则不需要压缩
            return false;
        }

        return true;
    }

    /**
     * 计算需要压缩的长宽值
     * 
     * @param srcWidth
     *            图片宽
     * @param srcHeight
     *            图片高
     * @param sysWidth
     *            屏幕宽
     * @param sysHight
     *            屏幕高
     * @return 计算结果:index 0: 宽 , 1: 高
     */
    private static int[] calculateCompressSize(int srcWidth, int srcHeight, int sysWidth, int sysHight) {
        int maxValue = srcWidth > srcHeight ? srcWidth : srcHeight;
        int minValue = srcWidth < srcHeight ? srcWidth : srcHeight;
        int compressImageWidth = srcWidth > srcHeight ? MAX_BITMAP_HEIGHT : MAX_BITMAP_WIDTH;
        int compressImageHeight = srcWidth > srcHeight ? MAX_BITMAP_WIDTH : MAX_BITMAP_HEIGHT;
        // 计算需要压缩的长宽值
        int resultWidth = 0, resultHeight = 0;
        // 计算长宽值算法规则
        // 1 定最长边，短边小于最短值符合
        // 2否则 定最短边，长边小于最长边大于屏幕，符合
        // 3否则 定屏幕宽，高度小于屏幕高，符合
        // 4否则 定屏幕高。
        // 1 定长边
        int smallLength = (int) (minValue * COMPRESS_IMAGE_OVER_BIG_LIMIT / maxValue);// 根据规定的长边计算短边
        if (maxValue > COMPRESS_IMAGE_OVER_BIG_LIMIT && smallLength < COMPRESS_IMAGE_OVER_SMALL_LIMIT) {
            if (maxValue == srcHeight) {
                resultHeight = (int) COMPRESS_IMAGE_OVER_BIG_LIMIT;
                resultWidth = smallLength;
            } else {
                resultWidth = (int) COMPRESS_IMAGE_OVER_BIG_LIMIT;
                resultHeight = smallLength;
            }
            return new int[] { resultWidth, resultHeight };
        }

        // 2 定短边
        int longLength = (int) (maxValue * COMPRESS_IMAGE_OVER_SMALL_LIMIT / minValue);// 根据规定的短边计算长边
        if (longLength <= COMPRESS_IMAGE_OVER_BIG_LIMIT
                && ((maxValue == srcHeight && longLength > compressImageHeight) || (maxValue == srcWidth && longLength > compressImageWidth))) {
            if (maxValue == srcHeight) {
                resultHeight = longLength;
                resultWidth = (int) COMPRESS_IMAGE_OVER_SMALL_LIMIT;
            } else {
                resultWidth = longLength;
                resultHeight = (int) COMPRESS_IMAGE_OVER_SMALL_LIMIT;
            }
            return new int[] { resultWidth, resultHeight };
        }

        // 3定宽
        if (srcWidth >= compressImageWidth) {
            resultWidth = compressImageWidth;
            resultHeight = srcHeight * compressImageWidth / srcWidth;
            if (resultHeight <= compressImageHeight) {
                return new int[] { resultWidth, resultHeight };
            }
        }

        // 4定高
        if (srcHeight >= compressImageHeight) {
            resultHeight = compressImageHeight;
            resultWidth = srcWidth * compressImageHeight / srcHeight;
            return new int[] { resultWidth, resultHeight };
        }
        return new int[] { srcWidth, srcHeight };
    }

    private static boolean writeStreamToFile(ByteArrayOutputStream out, String desFilePath) {
        boolean result = false;
        // 创建文件输出流，生成新的图片文件
        FileOutputStream os = null;
        try {
            File destFile = new File(desFilePath);
            os = new FileOutputStream(destFile);
            os.write(out.toByteArray());
            os.flush();
            os.close();
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != out) {
                try {
                    out.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }


        return result;
    }

    // 定义图片气泡的大小
    private static final int MAX_WIDTH_Y = 57;
    private static final int MAX_HEIGHT_X = 68;
    private static final int IMAGE_LENGHT = 130;
    private static final int NORMAL_WIDTH = 87;
    private static final int MAX_LENGHT = 120;

    /**
     * the type used for preview compressed image
     */
    public static final int PRE_IMAGE_TYPE = 0;
    /**
     * the type used for image message show on a conversion
     */
    public static final int MESSAGE_IMAGE_SHOW_TYPE = 1;

    /**
     * scale a bitmap
     * 
     * @param mContext
     *            the context object
     * @param bm
     *            the object of bitmap
     * @param scaleType
     *            scale type instead of when this method been used
     * @return the scaled image object
     */
    public static Bitmap zoomImg(Context mContext, Bitmap bm, int scaleType) {

        if (null == bm) {
            return null;
        }
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        float newWidth = 0.0f, newHeight = 0.0f;
        int maxLength = DisplayUtil.dip2px(mContext, IMAGE_LENGHT);
        if (width > height) {
            if (((float) width / height) >= 1.5f) {
                newHeight = DisplayUtil.dip2px(mContext, MAX_WIDTH_Y);
                newWidth = DisplayUtil.dip2px(mContext, MAX_LENGHT);
            } else {
                newHeight = DisplayUtil.dip2px(mContext, NORMAL_WIDTH);
                newWidth = newHeight * width / height;
                if (newWidth > maxLength) {
                    newWidth = maxLength;
                }
            }
        } else if (width < height) {
            if (((float) height / width) >= 1.5f) {
                newWidth = DisplayUtil.dip2px(mContext, MAX_HEIGHT_X);
                newHeight = DisplayUtil.dip2px(mContext, MAX_LENGHT);
            } else {
                newWidth = DisplayUtil.dip2px(mContext, NORMAL_WIDTH);
                newHeight = newWidth * height / width;
                if (newHeight > maxLength) {
                    newHeight = DisplayUtil.dip2px(mContext, IMAGE_LENGHT);
                }
            }
        } else {
            newWidth = DisplayUtil.dip2px(mContext, NORMAL_WIDTH);
            newHeight = DisplayUtil.dip2px(mContext, NORMAL_WIDTH);
        }

        if (PRE_IMAGE_TYPE == scaleType) {
            if (newHeight > newWidth) {
                // 减去5为了适配IOS气泡宽度问题
                newWidth = DisplayUtil.px2dip(mContext, newWidth) - 5;
            } else {
                newWidth = DisplayUtil.px2dip(mContext, newWidth);
            }
            newHeight = DisplayUtil.px2dip(mContext, newHeight);
        }

        // if (PRE_IMAGE_TYPE == scaleType) {
        // if (newHeight > newWidth) {
        // //减去5为了适配IOS气泡宽度问题
        // newWidth = newWidth - DisplayUtil.dip2px(mContext, 5);
        // }
        // }
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        if (scaleWidth > scaleHeight) {
            scaleHeight = scaleWidth;
        } else {
            scaleWidth = scaleHeight;
        }
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        int startX = (int) (newbm.getWidth() - newWidth) / 2;
        int startY = (int) (newbm.getHeight() - newHeight) / 2;
        // 对图片进行裁剪
        newbm = Bitmap.createBitmap(newbm, startX, startY, (int) newWidth, (int) newHeight);
        return newbm;
    }

    private static Bitmap compressBitmapInCaseOfOOM(String originalPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(originalPath, options);
        options.inSampleSize = 6;
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPreferredConfig = Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(originalPath, options);
        bitmap = adjustBitmapOrientation(bitmap, originalPath);
        return bitmap;
    }

    /**
     * Scale bitmap to the given size.
     * 
     * @param bitmap
     *            The bitmap to be scaled.
     * @param dstWidth
     *            Target width of bitmap to be scaled.
     * @param dstHeight
     *            Target height of bitmap to be scaled.
     * @return The scaled bitmap.
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int dstWidth, int dstHeight) {
        if (null == bitmap)
            return null;

        int srcWidth = bitmap.getWidth();
        int srcHeight = bitmap.getHeight();
        if (0 == srcWidth || 0 == srcHeight) {
            return bitmap;
        }

        float scaleWidth = ((float) dstWidth) / srcWidth;
        float scaleHeight = ((float) dstHeight) / srcHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, srcWidth, srcHeight, matrix, true);

        return dstbmp;
    }

    /**
     * Scale bitmap to the given ration.
     * 
     * @param bitmap
     *            The bitmap to be scaled.
     * @param widthRatio
     *            Target width ration of bitmap to be scaled.
     * @param heightRation
     *            Target height ration of bitmap to be scaled.
     * @return The scaled bitmap.
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, float widthRatio, float heightRation) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(widthRatio, heightRation); // 长和宽放大缩小的比例
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return dstbmp;
    }

    /*
     * Has not been used currently, private it
     */
    private static Bitmap cropSquareBitmap(Bitmap bitmap) {
        int oldWidth = bitmap.getWidth();
        int oldHeight = bitmap.getHeight();

        int newWidth = oldWidth > oldHeight ? oldHeight : oldWidth;
        int newHeight = oldWidth > oldHeight ? oldHeight : oldWidth;

        int retX = oldWidth == newWidth ? 0 : (oldWidth - newWidth) / 2;
        int retY = oldWidth == newWidth ? (oldHeight - newHeight) / 2 : 0;
        return Bitmap.createBitmap(bitmap, retX, retY, newWidth, newHeight, null, false);
    }

    /**
     * Create a new bitmap with rounded corner using the given source bitmap.
     * Also see {@link #getRoundedCornerBitmapSafe(Bitmap, float)}
     * 
     * @param sourceBitmap
     *            The source bitmap.
     * @param roundPx
     *            The x-radius and y-radius of the oval used to round the
     *            corners.
     * @return The new bitmap with rounded corner, if create the new bitmap
     *         successfully. <br>
     *         null, if failed to create the new bitmap.
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap sourceBitmap, float roundPx) {
        try {
            Bitmap output = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(sourceBitmap, rect, rect, paint);
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create a new bitmap with rounded corner using the given source bitmap and
     * recycle the source bitmap. Also see
     * {@link #getRoundedCornerBitmap(Bitmap, float)}
     * 
     * @param sourceBitmap
     *            The source bitmap.
     * @param
     *            x-radius and y-radius of the oval used to round the corners.
     * @return The new bitmap with rounded corner, if create the new bitmap
     *         successfully. <br>
     *         null, if failed to create the new bitmap.
     */
    public static Bitmap getRoundedCornerBitmapSafe(Bitmap sourceBitmap, float roundPx) {
        Bitmap oldBitmap = sourceBitmap;
        sourceBitmap = getRoundedCornerBitmap(oldBitmap, roundPx);

        if (null == sourceBitmap) {
            return oldBitmap;
        }

        oldBitmap.recycle();
        oldBitmap = null;

        return sourceBitmap;
    }

    /**
     * Create a new circle bitmap using the given source bitmap. Also see
     * {@link #getCircleBitmapSafe(Bitmap)}
     * 
     * @param sourceBitmap
     *            The source bitmap.
     * @return The circle bitmap. <br>
     *         null, if failed to create circle bitmap.
     */
    public static Bitmap getCircleBitmap(Bitmap sourceBitmap) {
        try {
            if (null == sourceBitmap) {
                return null;
            }

            int width = sourceBitmap.getWidth();
            int height = sourceBitmap.getHeight();

            Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            if (null == output) {
                return null;
            }

            Canvas canvas = new Canvas(output);

            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, width, height);

            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setDither(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.parseColor("#BAB399"));
            canvas.drawCircle(width / 2 + 0.7f, height / 2 + 0.7f, width / 2 + 0.1f, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(sourceBitmap, rect, rect, paint);

            return output;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a new circle bitmap using the given source bitmap and recycle the
     * source bitmap. Also see {@link #getCircleBitmap(Bitmap)}
     * 
     * @param sourceBitmap
     *            The source bitmap.
     * @return The circle bitmap. <br>
     *         null, if failed to create circle bitmap.
     */
    public static Bitmap getCircleBitmapSafe(Bitmap sourceBitmap) {
        Bitmap oldBitmap = sourceBitmap;
        sourceBitmap = getCircleBitmap(oldBitmap);

        if (null == sourceBitmap) {
            return oldBitmap;
        }

        oldBitmap.recycle();
        oldBitmap = null;

        return sourceBitmap;
    }

    private static BitmapFactory.Options getBitmapOptions(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        return opts;
    }

    /*
     * Has not been used currently, so private it
     */
    private static Bitmap scaleBitmap(int id, Activity activity) {
        Bitmap _bitmapPreScale = BitmapFactory.decodeResource(activity.getResources(), id);

        Rect rectgle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
        int normalStatusBarHeight = rectgle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int TitleBarHeight = contentViewTop - normalStatusBarHeight;

        int statusBarHeight = (int) Math.ceil(25 * activity.getResources().getDisplayMetrics().density);

        int oldWidth = _bitmapPreScale.getWidth();
        int oldHeight = _bitmapPreScale.getHeight();
        int newWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        int newHeight = activity.getWindowManager().getDefaultDisplay().getHeight() - statusBarHeight;

        if (isHoneycombTablet(activity)) {
            newHeight = activity.getWindowManager().getDefaultDisplay().getHeight() - normalStatusBarHeight;
        }

        // calculate the scale
        float scaleWidth = ((float) newWidth) / oldWidth;
        float scaleHeight = ((float) newHeight) / oldHeight;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        return Bitmap.createBitmap(_bitmapPreScale, 0, 0, oldWidth, oldHeight, matrix, true);
    }

    /*
     * Has not been used currently, so private it
     */
    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /*
     * Has not been used currently, so private it
     */
    private static boolean isHoneycombTablet(Context context) {
        return isHoneycomb() && isTablet(context);
    }

    /*
     * Has not been used currently, so private it
     */
    private static boolean isHoneycomb() {
        // Can use static final constants like HONEYCOMB, declared in later
        // versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= 11;
    }

    /*
     * Has not been used currently, so private it
     */
    private static Bitmap createBitMap(String picturePath) {
        Bitmap thumbnail = adjustImage(picturePath);
        if (thumbnail == null) {
            return null;
        }

        int degree = getOrientationRotateDegree(picturePath);
        if (0 != degree) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            thumbnail = Bitmap
                    .createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, false);
        }
        return thumbnail;
    }

    /*
     * Has not been used currently, so private it
     */
    private static Bitmap adjustImage(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        int srcWidth = opts.outWidth;
        int srcHeight = opts.outHeight;
        int maxValue = srcWidth > srcHeight ? srcWidth : srcHeight;
        // 缩放的比例
        double ratio = MAX_IMAGE_VALUE / maxValue;

        Bitmap compressedBitmap = null;
        try {
            Bitmap thumbnailBitmap = getThumbnail(path, (int) (ratio * srcWidth), (int) (ratio * srcHeight));
            if (null == thumbnailBitmap) {
                return null;
            }
            BitmapFactory.Options tempOpts = new BitmapFactory.Options();
            tempOpts.inJustDecodeBounds = false;
            tempOpts.inSampleSize = 1;

            tempOpts.inPurgeable = true;
            tempOpts.inInputShareable = true;
            tempOpts.inPreferredConfig = Config.ARGB_4444;
            compressedBitmap = ThumbnailUtils.extractThumbnail(thumbnailBitmap, (int) (ratio * srcWidth),
                    (int) (ratio * srcHeight), 1);
        } catch (Error e) {
            e.printStackTrace();
            return null;
        }

        return compressedBitmap;
    }

    /*
     * Has not been used currently, private it.
     */
    private static Bitmap adjustDisplayImage(String path, int width, int hight) {
        BitmapFactory.Options opts = getBitmapOptions(path);
        int srcWidth = opts.outWidth;
        int srcHeight = opts.outHeight;

        Bitmap compressedBitmap = null;
        Bitmap thumbnailBitmap = null;
        try {
            thumbnailBitmap = getThumbnail(path, width, hight);
            if (null == thumbnailBitmap) {
                return null;
            }
            compressedBitmap = ThumbnailUtils.extractThumbnail(thumbnailBitmap, width, hight, 1);
        } catch (Error e) {

            return null;
        } finally {
            if (thumbnailBitmap != null) {
                thumbnailBitmap.recycle();
                thumbnailBitmap = null;
            }
        }

        return compressedBitmap;
    }

    /*
     * Has not been used currently, so private it
     */
    private static Bitmap getThumbnail(String filePath, int targetW, int targetH) {
        Bitmap bitmap = null;

        File file = new File(filePath);
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") < 0) {
            return bitmap;
        }

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        if (fileExtension.equalsIgnoreCase(IMAGE_TYPT_BMP) || fileExtension.equalsIgnoreCase(IMAGE_TYPT_JPG)
                || fileExtension.equalsIgnoreCase(IMAGE_TYPT_JPEG) || fileExtension.equalsIgnoreCase(IMAGE_TYPT_PNG)
                || fileExtension.equalsIgnoreCase(IMAGE_TYPT_GIF)) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            int bitmapRealWidth = options.outWidth;
            int bitmapRealHeight = options.outHeight;
            options.outWidth = targetW;
            if (bitmapRealWidth > 0) {
                options.outHeight = bitmapRealHeight * targetW / bitmapRealWidth;
            }

            options.inJustDecodeBounds = false;

            if (targetW > 0) {
                options.inSampleSize = bitmapRealWidth / targetW;
            }

            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inPreferredConfig = Config.ARGB_4444;

            bitmap = BitmapFactory.decodeFile(filePath, options);
        }

        return bitmap;
    }

    /**
     * judge a image format is gif or not
     * 
     * @param imageName
     *            image'name
     * @return true is gif format, false isn't gif format
     */
    public static boolean isGifFormatImage(String imageName) {
        String string = imageName.substring(imageName.lastIndexOf(".") + 1);
        if (string.equalsIgnoreCase(IMAGE_TYPT_GIF)) {
            return true;
        } else {
            return false;
        }
    }
}
