package com.xishuang.imageviewprocess.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xishuang.imageviewprocess.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:xishuang
 * Date:2018.04.03
 * Des: PorterDuffColorFilter的效果调试界面
 */
public class PorterDuffActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar sBR;
    private SeekBar sBG;
    private SeekBar sBB;
    private SeekBar sBA;
    private ImageView imageView;
    private TextView tvColorText;
    private TextView tvColor;

    private PorterDuff.Mode mode = PorterDuff.Mode.DST;
    private int mColor;

    private List<? extends Map<String, ?>> mDataList;
    private TextView tvModeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porter_duff);

        imageView = (ImageView) findViewById(R.id.porter_duff_img);
        //SeekBar
        sBA = (SeekBar) findViewById(R.id.porter_duff_bar_A);
        sBR = (SeekBar) findViewById(R.id.porter_duff_bar_R);
        sBG = (SeekBar) findViewById(R.id.porter_duff_bar_G);
        sBB = (SeekBar) findViewById(R.id.porter_duff_bar_B);

        //选中的颜色值
        tvColorText = (TextView) findViewById(R.id.porter_duff_color_text);
        tvModeText = (TextView) findViewById(R.id.porter_duff_mode);
        tvColor = (TextView) findViewById(R.id.porter_duff_color);
        //RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.porter_duff_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PorterDuffAdapter());

        sBA.setOnSeekBarChangeListener(this);
        sBR.setOnSeekBarChangeListener(this);
        sBG.setOnSeekBarChangeListener(this);
        sBB.setOnSeekBarChangeListener(this);

        mDataList = getData();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String addText = "源颜色值(ARGB)：#" + Integer.toHexString(sBA.getProgress()) + "-"
                + Integer.toHexString(sBR.getProgress()) + "-"
                + Integer.toHexString(sBG.getProgress()) + "-"
                + Integer.toHexString(sBB.getProgress());
        mColor = Color.argb(sBA.getProgress(), sBR.getProgress(), sBG.getProgress(), sBB.getProgress());
        tvColorText.setText(addText);
        tvColor.setBackgroundColor(mColor);
        //关键代码，设置PorterDuffColorFilter
        imageView.setColorFilter(new PorterDuffColorFilter(mColor, mode));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 数据初始化
     */
    private List<? extends Map<String, ?>> getData() {
        List<Map<String, Object>> data = new ArrayList<>();
        addItem(data, "CLEAR(Alpha合成)", PorterDuff.Mode.CLEAR);
        addItem(data, "SRC(Alpha合成)", PorterDuff.Mode.SRC);
        addItem(data, "DST(Alpha合成)", PorterDuff.Mode.DST);
        addItem(data, "SRC_OVER(Alpha合成)", PorterDuff.Mode.SRC_OVER);
        addItem(data, "DST_OVER(Alpha合成)", PorterDuff.Mode.DST_OVER);
        addItem(data, "SRC_IN(Alpha合成)", PorterDuff.Mode.SRC_IN);
        addItem(data, "DST_IN(Alpha合成)", PorterDuff.Mode.DST_IN);
        addItem(data, "SRC_OUT(Alpha合成)", PorterDuff.Mode.SRC_OUT);
        addItem(data, "DST_OUT(Alpha合成)", PorterDuff.Mode.DST_OUT);
        addItem(data, "SRC_ATOP(Alpha合成)", PorterDuff.Mode.SRC_ATOP);
        addItem(data, "DST_ATOP(Alpha合成)", PorterDuff.Mode.DST_ATOP);
        addItem(data, "DARKEN(混合)", PorterDuff.Mode.DARKEN);
        addItem(data, "LIGHTEN(混合)", PorterDuff.Mode.LIGHTEN);
        addItem(data, "MULTIPLY(混合)", PorterDuff.Mode.MULTIPLY);
        addItem(data, "SCREEN(混合)", PorterDuff.Mode.SCREEN);
        addItem(data, "ADD(混合)", PorterDuff.Mode.ADD);
        addItem(data, "OVERLAY(混合)", PorterDuff.Mode.OVERLAY);

        return data;
    }

    private void addItem(List<Map<String, Object>> data, String title, PorterDuff.Mode value) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("value", value);
        data.add(map);
    }

    class PorterDuffAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PorterDuffActivity.this).inflate(R.layout.activity_porter_duff_item, parent, false);
            return new PorterDuffViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof PorterDuffViewHolder) {
                PorterDuffViewHolder porterDuffViewHolder = (PorterDuffViewHolder) holder;
                porterDuffViewHolder.itemTextView.setText((position + 1) + "、" + mDataList.get(position).get("title"));
                porterDuffViewHolder.itemTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        mode = (PorterDuff.Mode) mDataList.get(position).get("value");
                        Toast.makeText(PorterDuffActivity.this, (String) mDataList.get(position).get("title"), Toast.LENGTH_SHORT).show();
                        imageView.setColorFilter(new PorterDuffColorFilter(mColor, mode));
                        tvModeText.setText("模式：" + mDataList.get(position).get("title"));
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

    class PorterDuffViewHolder extends RecyclerView.ViewHolder {

        TextView itemTextView;

        PorterDuffViewHolder(View itemView) {
            super(itemView);
            itemTextView = (TextView) itemView.findViewById(R.id.porter_duff_item_text);
        }
    }
}
