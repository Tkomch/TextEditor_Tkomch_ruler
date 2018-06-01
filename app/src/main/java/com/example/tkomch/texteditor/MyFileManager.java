package com.example.tkomch.texteditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.EGLExt;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyFileManager extends AppCompatActivity {

    private Button up_return;
    private Button return_btn;
    private ListView file_list;             //显示文件的控件
    private ArrayAdapter<String> file_adapter;
    private List<String> list_string;
    private Context c = this;
    private Activity ac = MyFileManager.this;
    private String[] file_name_list;
    private FileRead fr;
//    private input_box ib;
    private String file_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_file_manager);

        //用于接受Activity1的PATH
        Intent i = getIntent();
        Bundle b = i.getExtras();
        file_path = b.getString("path");

        if(file_path == null){
            fr = new FileRead(ac);
        }
        else{
            fr = new FileRead(file_path,ac);
        }

        up_return = (Button)findViewById(R.id.up_return);            //返回上一层按钮
        return_btn = (Button)findViewById(R.id.return_btn);    //返回的按钮l

        file_list = (ListView)findViewById(R.id.file_list);
        list_string = new ArrayList<String>();
        file_adapter = new ArrayAdapter<String>(ac, android.R.layout.simple_list_item_1,list_string);

        //绘制LIstView
        new flush_listview(fr, list_string, file_name_list, file_adapter,file_list);

        //返回上一层
        up_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上一层操作
                //判断是否是根目录
                if(fr.get_read_dir().equals(fr.sd_dir + File.separator)){
                    new AlertDialog.Builder(ac).setTitle("已经是最顶层").create().show();
                }
                else{
                    //返回上一层操作
                    //处理字符串得到上一层的路径
                    String[] up_dir = fr.get_read_dir().split("/");
                    String up_path = File.separator + up_dir[1];
                    for(int i = 2;i < up_dir.length-1;i++){
                        up_path = up_path + File.separator + up_dir[i];
                    }
                    up_path += File.separator;

                    //进入upath
                    fr = new FileRead(up_path,ac);

                    new flush_listview(fr, list_string, file_name_list, file_adapter,file_list);

                }
            }
        });

        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上一界面
                //用于传回read_dir
                Intent i2 = new Intent();
                i2.putExtra("nowdir",fr.get_read_dir());
                setResult(1,i2);
                finish();
            }
        });

        file_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取点击的PATH
                String path_choose = fr.get_read_dir() + String.valueOf(file_adapter.getItem(position)).split("\n")[0];
                File file_choose = new File(path_choose);
                if(fr.isDir(file_choose)){
                    path_choose += File.separator;
                    //进入path_choose
                    fr = new FileRead(path_choose,ac);
                    new flush_listview(fr, list_string, file_name_list, file_adapter,file_list);
                }
                else{
                    //打开文件操作
                    Intent i3 = new Intent();
                    i3.putExtra("openfile",path_choose);
                    i3.setClass(ac,Editor.class);
                    startActivity(i3);
                    finish();
                }
            }
        });
    }

    public Activity getAc() {
        return ac;
    }

    public FileRead getFr() {
        return fr;
    }

    public List<String> getList_string() {
        return list_string;
    }

    public ArrayAdapter<String> getFile_adapter() {
        return file_adapter;
    }

    public ListView getFile_list() {
        return file_list;
    }

    public String[] getFile_name_list() {
        return file_name_list;
    }

    public Context getC() {
        return c;
    }
}
