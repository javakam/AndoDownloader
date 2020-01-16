package com.ando.download;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ando.download.demo.DemoActivity;
import com.ando.download.many.ManyTaskActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * Title:MainActivity
 * <pre>
 *      https://github.com/lingochamp/okdownload/wiki/Simple-Use-Guideline
 *
 *      https://www.cnblogs.com/baiqiantao/p/10679677.html
 * </pre>
 *
 * @author Changbao
 * @date 2020/1/15 14:48
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
        findViewById(R.id.tv1).setOnClickListener(this);
        findViewById(R.id.tv2).setOnClickListener(this);
        findViewById(R.id.tv3).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.tv1:
                startActivity(new Intent(this, DemoActivity.class));
                break;
            case R.id.tv2:
                break;
            case R.id.tv3:// "ヾ(o◕∀◕)ﾉヾ 多任务下载 ヾ(o◕∀◕)ﾉヾ ",
                startActivity(new Intent(this, ManyTaskActivity.class));
                break;
            default:
        }
    }



}