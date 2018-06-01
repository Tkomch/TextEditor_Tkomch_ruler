package com.example.tkomch.texteditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

public class ChooseDir extends AppCompatActivity {

    private Button newdir = null;
    private Button return_btn = null;
    private Button choose_dir = null;
    private Button del_btn;
    private Button up_return;
    private ListView file_list;
    private ArrayAdapter<String> file_adapter;
    private List<String> list_string;
    private Context c = this;
    private Activity ac = ChooseDir.this;
    private String[] file_name_list = null;
    private FileRead fr = null;
    //private input_box ib = null;
    private String file_path = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dir);

        newdir = (Button)findViewById(R.id.newdir);        //新建文件夹
        return_btn = (Button)findViewById(R.id.return_btn); //返回
        choose_dir = (Button)findViewById(R.id.choosedir);   //选择文件夹
        del_btn = (Button)findViewById(R.id.del_btn);       //删除文件或文件夹
        up_return = (Button)findViewById(R.id.up_return);   //返回上一层按钮
        file_list = (ListView)findViewById(R.id.file_list); //文件的显示列表
        final ChooseDir cd = this;

        //接受Activity1的path
        Intent i = getIntent();
        Bundle b = i.getExtras();
        file_path = b.getString("path");

        if(file_path == null){
            fr = new FileRead(ac);
        }
        else{
            fr = new FileRead(file_path,ac);
        }

        file_list = (ListView)findViewById(R.id.file_list);
        list_string = new ArrayList<String>();
        file_adapter = new ArrayAdapter<String>(ac, android.R.layout.simple_list_item_1,list_string);

        new flush_listview(fr, list_string, file_name_list, file_adapter,file_list);

        //新建文件夹
        newdir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新建文件夹操作
                input_box ib = new input_box(cd);              //初始化对话框
                ib.setBox(0);
            }
        });

        //选择文件夹
        choose_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //弹出输入文件名
                input_box ib = new input_box(cd);
                ib.setBox(1);
            }
        });

        //删除文件或文件夹
        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //文件夹的删除操作
                input_box ib = new input_box(cd);
                ib.setBox(2);
            }
        });

        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent();
                i2.putExtra("nowdir",fr.get_read_dir());
                setResult(1,i2);
                //返回上一界面
                finish();
            }
        });

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
