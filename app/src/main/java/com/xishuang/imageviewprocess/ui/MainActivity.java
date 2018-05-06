package com.xishuang.imageviewprocess.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.xishuang.imageviewprocess.R;
import com.xishuang.imageviewprocess.security.DecryptUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView contentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_colorfilter);

        findViewById(R.id.bt_lighting).setOnClickListener(this);
        findViewById(R.id.bt_porterduff).setOnClickListener(this);
        findViewById(R.id.bt_colormatrix).setOnClickListener(this);

        contentTv = (TextView) findViewById(R.id.bt_text);

        inputData();
    }

    private void inputData() {
        InputStream in = DecryptUtil.onObtainInputStream(this);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GBK"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            contentTv.setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_lighting) {
            Intent intent = new Intent(this, LightingActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.bt_porterduff) {
            Intent intent = new Intent(this, PorterDuffActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.bt_colormatrix) {
            Intent intent = new Intent(this, ColorMatrixActivity.class);
            startActivity(intent);
        }
    }
}
