package com.xishuang.imageviewprocess.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.xishuang.imageviewprocess.algorithm.SobelAlgorithm;
import com.xishuang.imageviewprocess.algorithm.SpecialColorMatrix;

/**
 * Author : xishuang
 * Date   : 2018.04.02
 * Desc   : 图片处理中心
 */
public class ImageUtil {
    private static ColorMatrix colorMatrix = new ColorMatrix();
    /**
     * 色调，改变颜色
     */
    private static ColorMatrix hueMatrix = new ColorMatrix();
    /**
     * 饱和度，改变颜色的纯度
     */
    private static ColorMatrix saturationMatrix = new ColorMatrix();
    /**
     * 亮度，控制明暗
     */
    private static ColorMatrix brightnessMatrix = new ColorMatrix();

    /**
     * 使用Sobel算法边缘检测
     */
    public static void displayImageSobel(Context context, ImageView imageView, int res_id) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res_id);
        bitmap = SobelAlgorithm.Sobel(bitmap);
        imageView.setImageBitmap(bitmap);
    }

    /**
     * 通过乘积和加来操作的LightingColorFilter
     *
     * @param mul 乘积分量
     * @param add 加法分量
     */
    public static void displayImageLighting(ImageView imageView, int mul, int add) {
        imageView.setColorFilter(new LightingColorFilter(mul, add));
    }

    /**
     * 通过源颜色值和PorterDuff模式来操作的PorterDuffColorFilter
     *
     * @param color 源颜色值
     * @param mode  PorterDuff模式
     */
    public static void displayImagePorterDuff(ImageView imageView, @ColorInt int color, @NonNull PorterDuff.Mode mode) {
        imageView.setColorFilter(new PorterDuffColorFilter(color, mode));
    }

    /**
     * 通过色调来操作ImageView的ColorMatrixColorFilter
     *
     * @param hueValue 色调值
     */
    public static void displayImageColorMatrixH(ImageView imageView, float hueValue) {
        displayImageColorMatrixHSB(imageView, hueValue, 1, 1);
    }

    /**
     * 通过饱和度来操作ImageView的ColorMatrixColorFilter
     *
     * @param saturationValue 饱和度值
     */
    public static void displayImageColorMatrixS(ImageView imageView, float saturationValue) {
        displayImageColorMatrixHSB(imageView, 0, saturationValue, 1);
    }

    /**
     * 通过亮度来操作ImageView的ColorMatrixColorFilter
     *
     * @param brightnessValue 亮度值
     */
    public static void displayImageColorMatrixB(ImageView imageView, float brightnessValue) {
        displayImageColorMatrixHSB(imageView, 0, 1, brightnessValue);
    }

    /**
     * 通过色调、饱和度、亮度来操作ImageView的ColorMatrixColorFilter
     *
     * @param hueValue        色调值
     * @param saturationValue 饱和度值
     * @param brightnessValue 亮度值
     */
    public static void displayImageColorMatrixHSB(ImageView imageView, float hueValue, float saturationValue, float brightnessValue) {
        //设置色相，为0°和360的时候相当于原图
        hueMatrix.reset();
        hueMatrix.setRotate(0, hueValue);
        hueMatrix.setRotate(1, hueValue);
        hueMatrix.setRotate(2, hueValue);

        //设置饱和度，为1的时候相当于原图
        saturationMatrix.reset();
        saturationMatrix.setSaturation(saturationValue);

        //亮度，为1的时候相当于原图
        brightnessMatrix.reset();
        brightnessMatrix.setScale(brightnessValue, brightnessValue, brightnessValue, 1);

        //将上面三种效果和选中的模式混合在一起
        colorMatrix.reset();
        colorMatrix.postConcat(hueMatrix);
        colorMatrix.postConcat(saturationMatrix);
        colorMatrix.postConcat(brightnessMatrix);

        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    /**
     * 针对特定ColorMatrixColorFilter实现特殊效果
     */
    public static void displayImageColorMatrix(ImageView imageView, int mode) {
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
    }
}
