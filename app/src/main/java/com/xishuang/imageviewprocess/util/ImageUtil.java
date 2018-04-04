package com.xishuang.imageviewprocess.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.xishuang.imageviewprocess.algorithm.SobelAlgorithm;
import com.xishuang.imageviewprocess.algorithm.SpecialColorMatrix;

/**
 * Author : xishuang
 * Date   : 2018.04.02
 * Desc   :
 */
public class ImageUtil {
    /**
     * 使用Sobel算法边缘检测
     */
    public static void displayImageSobel(Context context, ImageView imageView, int res_id) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res_id);
        bitmap = SobelAlgorithm.Sobel(bitmap);
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 针对特定ColorMatrix实现特殊效果
     */
    public static float[] displayImageColorMatrix(ImageView imageView, int mode) {
        float[] matrix = SpecialColorMatrix.getDefault();
        switch (mode) {
            case SpecialColorMatrix.MODE.DEFAULT:
                matrix = SpecialColorMatrix.getDefault();
                break;
            case SpecialColorMatrix.MODE.HUAIJIU:
                matrix = SpecialColorMatrix.getHuaiJiu();
                break;
            case SpecialColorMatrix.MODE.DIPIAN:
                matrix = SpecialColorMatrix.getDiPian();
                break;
            case SpecialColorMatrix.MODE.GRAY:
                matrix = SpecialColorMatrix.getGray();
                break;
            case SpecialColorMatrix.MODE.BRIGHT:
                matrix = SpecialColorMatrix.getBright();
                break;
            default:
        }
        imageView.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(matrix)));
        return matrix;
    }
}
