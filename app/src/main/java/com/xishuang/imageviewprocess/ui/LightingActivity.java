package com.xishuang.imageviewprocess.ui;

import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xishuang.imageviewprocess.R;

/**
 * Author:xishuang
 * Date:2018.04.03
 * Des: LightingColorFilter的效果调试界面
 */
public class LightingActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private ImageView imageView;
    private SeekBar sBPlusA;
    private SeekBar sBPlusR;
    private SeekBar sBPlusG;
    private SeekBar sBPlusB;
    private SeekBar sBAddA;
    private SeekBar sBAddR;
    private SeekBar sBAddG;
    private SeekBar sBAddB;
    private TextView tvPlusColorText;
    private TextView tvPlusColor;
    private TextView tvAddColorText;
    private TextView tvAddColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lighting);

        imageView = (ImageView) findViewById(R.id.lighting_img);

        sBPlusA = (SeekBar) findViewById(R.id.lighting_plus_bar_A);
        sBPlusR = (SeekBar) findViewById(R.id.lighting_plus_bar_R);
        sBPlusG = (SeekBar) findViewById(R.id.lighting_plus_bar_G);
        sBPlusB = (SeekBar) findViewById(R.id.lighting_plus_bar_B);

        sBAddA = (SeekBar) findViewById(R.id.lighting_add_bar_A);
        sBAddR = (SeekBar) findViewById(R.id.lighting_add_bar_R);
        sBAddG = (SeekBar) findViewById(R.id.lighting_add_bar_G);
        sBAddB = (SeekBar) findViewById(R.id.lighting_add_bar_B);

        tvPlusColorText = (TextView) findViewById(R.id.lighting_plus_color_text);
        tvPlusColor = (TextView) findViewById(R.id.lighting_plus_color);
        tvAddColorText = (TextView) findViewById(R.id.lighting_add_color_text);
        tvAddColor = (TextView) findViewById(R.id.lighting_add_color);

        sBPlusA.setOnSeekBarChangeListener(this);
        sBPlusR.setOnSeekBarChangeListener(this);
        sBPlusG.setOnSeekBarChangeListener(this);
        sBPlusB.setOnSeekBarChangeListener(this);
        sBAddA.setOnSeekBarChangeListener(this);
        sBAddR.setOnSeekBarChangeListener(this);
        sBAddG.setOnSeekBarChangeListener(this);
        sBAddB.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        //透明度也加上，透明度不参与计算，所以设置了也是默认无效的
        String plusText = "Plus颜色值(ARGB)：#" + Integer.toHexString(sBPlusA.getProgress()) + "-"
                + Integer.toHexString(sBPlusR.getProgress()) + "-"
                + Integer.toHexString(sBPlusG.getProgress()) + "-"
                + Integer.toHexString(sBPlusB.getProgress());
        int plusColor = Color.argb(sBPlusA.getProgress(), sBPlusR.getProgress(), sBPlusG.getProgress(), sBPlusB.getProgress());
        tvPlusColorText.setText(plusText);
        tvPlusColor.setBackgroundColor(plusColor);

        String addText = "Add颜色值(ARGB)：#" + Integer.toHexString(sBAddA.getProgress()) + "-"
                + Integer.toHexString(sBAddR.getProgress()) + "-"
                + Integer.toHexString(sBAddG.getProgress()) + "-"
                + Integer.toHexString(sBAddB.getProgress());
        int addColor = Color.argb(sBAddA.getProgress(), sBAddR.getProgress(), sBAddG.getProgress(), sBAddB.getProgress());
        tvAddColorText.setText(addText);
        tvAddColor.setBackgroundColor(addColor);

        //关键代码，设置LightingColorFilter
        imageView.setColorFilter(new LightingColorFilter(plusColor, addColor));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
