package com.xishuang.imageviewprocess.algorithm;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Author : xishuang
 * Date   : 2018.04.02
 * Desc   : Sobel边缘检测算法
 */
public class SobelAlgorithm {
    /**
     * Sobel算法
     */
    public static Bitmap Sobel(Bitmap bitmap) {

        bitmap = compress(bitmap, 480, 800);
        Bitmap temp = toGrayscale(bitmap);
        int w = temp.getWidth();
        int h = temp.getHeight();

        int[] mmap = new int[w * h];
        double[] tmap = new double[w * h];
        int[] cmap = new int[w * h];

        temp.getPixels(mmap, 0, temp.getWidth(), 0, 0, temp.getWidth(),
                temp.getHeight());

        double max = Double.MIN_VALUE;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                double gx = GX(i, j, temp);
                double gy = GY(i, j, temp);
                tmap[j * w + i] = Math.sqrt(gx * gx + gy * gy);
                if (max < tmap[j * w + i]) {
                    max = tmap[j * w + i];
                }
            }
        }

        double top = max * 0.06;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (tmap[j * w + i] > top) {
                    cmap[j * w + i] = mmap[j * w + i];
                } else {
                    cmap[j * w + i] = Color.WHITE;
                }
            }
        }
        return Bitmap.createBitmap(cmap, temp.getWidth(), temp.getHeight(),
                Bitmap.Config.ARGB_8888);
    }

    /**
     * 获取横向的
     *
     * @param x 第x行
     * @param y 第y列
     */
    public static double GX(int x, int y, Bitmap bitmap) {
        double res = (-1) * getPixel(x - 1, y - 1, bitmap) + 1
                * getPixel(x + 1, y - 1, bitmap) + (-Math.sqrt(2))
                * getPixel(x - 1, y, bitmap) + Math.sqrt(2)
                * getPixel(x + 1, y, bitmap) + (-1)
                * getPixel(x - 1, y + 1, bitmap) + 1
                * getPixel(x + 1, y + 1, bitmap);
        return res;
    }

    /**
     * 获取纵向的
     *
     * @param x 第x行
     * @param y 第y列
     */
    public static double GY(int x, int y, Bitmap bitmap) {
        double res = 1 * getPixel(x - 1, y - 1, bitmap) + Math.sqrt(2)
                * getPixel(x, y - 1, bitmap) + 1
                * getPixel(x + 1, y - 1, bitmap) + (-1)
                * getPixel(x - 1, y + 1, bitmap) + (-Math.sqrt(2))
                * getPixel(x, y + 1, bitmap) + (-1)
                * getPixel(x + 1, y + 1, bitmap);
        return res;
    }

    /**
     * 获取第x行第y列的色度
     *
     * @param x 第x行
     * @param y 第y列
     */
    public static double getPixel(int x, int y, Bitmap bitmap) {
        if (x < 0 || x >= bitmap.getWidth() || y < 0 || y >= bitmap.getHeight())
            return 0;
        return bitmap.getPixel(x, y);
    }

    /**
     * 转化成灰度图
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {

        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * Bitmap压缩
     */
    public static Bitmap compress(final Bitmap bm, int reqWidth, int reqHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        if (height > reqHeight || width > reqWidth) {
            float scaleWidth = (float) reqWidth / width;
            float scaleHeight = (float) reqHeight / height;
            float scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap result = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
            bm.recycle();
            return result;
        }
        return bm;
    }
}
