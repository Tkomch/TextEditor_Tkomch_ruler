package com.example.tkomch.texteditor;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class Activity1 extends AppCompatActivity {

    private ListView text_list = null;         //显示文件的列表
    private Button open_btn = null;     //打开文件按钮
    private Button new_btn = null;      //新建文件按钮
    private String file_path = null;    //从文件管理返回的文件路径

    private Intent i_open = null;                    //点击打开跳转界面时用于传递信息
    private Intent i_new = null;                     //点击新建跳转界面时用于传递信息

    private TextView test_info;

    private Activity activity = Activity1.this;

    private Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        text_list = (ListView)findViewById(R.id.text_list);
        open_btn = (Button)findViewById(R.id.open_btn);
        new_btn = (Button)findViewById(R.id.new_btn);
        test_info = (TextView) findViewById(R.id.testinfo);

        /*
        //显示缓存的文件
        String[] file_names = c.fileList();
        String file_name = file_names[0];
        for(int i = 1;i < c.fileList().length;i++){
            file_name = file_name + "\n" + file_names[i];
        }

        test_info.setText(file_name);
        */
        //打开按钮触发事件
        open_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入文件选择

                i_open = new Intent();
                i_open.setClass(Activity1.this,MyFileManager.class);
                i_open.putExtra("path",file_path);
                startActivityForResult(i_open,0);
            }
        });

        //新建文件触发事件
        new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入文件夹选择
                i_new = new Intent();
                i_new.setClass(Activity1.this,ChooseDir.class);
                i_new.putExtra("path",file_path);
                startActivityForResult(i_new,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0 && resultCode == 1){
            //从上一Activity获取路径
            file_path = data.getStringExtra("nowdir");
        }
    }
}
