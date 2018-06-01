package com.example.tkomch.texteditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class input_box{
    //变量
    private LayoutInflater factory;
    private String title[] = {"新建文件夹","新建文件","删除"};
    private ChooseDir cd;               //弹出的对话框所在的窗体
    private MyFileManager mfm;
    private Activity a;
    private FileRead fr;
    private boolean isChooseDir = true;
    private boolean isCloseAc = false;

    public input_box(ChooseDir t) {
        cd = t;
        a = t.getAc();
        fr = t.getFr();
    }

    public input_box(MyFileManager t){
        mfm = t;
        a = mfm.getAc();
        fr = mfm.getFr();
        isChooseDir = false;
    }

    public void setBox(int node_in){
        //将布局文件放在activity中
        factory = LayoutInflater.from(a);
        final String path = fr.get_read_dir();
        final int node = node_in;
        final View v  = factory.inflate(R.layout.edittext,null);
        final EditText in = (EditText)v.findViewById(R.id.edit);
        Button ok = (Button)v.findViewById(R.id.ok);
        final Button no = (Button) v.findViewById(R.id.no);

        final AlertDialog.Builder builder = new AlertDialog.Builder(a);
        builder.setTitle(title[node]).setView(v).create();
        final AlertDialog box = builder.show();

        //确定按钮
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(in.getText().equals("")){
                    box.dismiss();
                    new AlertDialog.Builder(a).setTitle("名字不能为空！").create().show();
                }
                File f = new File(path + in.getText());
                if(node == 0){                 //新建文件夹
                    if(!f.exists())
                        f.mkdirs();
                    else{
                        new AlertDialog.Builder(a).setTitle("文件夹已经存在！").create().show();
                    }
                }
                else if(node == 1){    //新建文件
                    if(!f.exists()){
                        try {
                            f.createNewFile();
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                        //进入下一个Activity
                        Intent i1 = new Intent();
                        if(isChooseDir){
                            i1.setClass(cd.getC(),Editor.class);
                            i1.putExtra("openfile",path + in.getText());
                            cd.startActivity(i1);
                            cd.finish();
                        }
                        else{
                            i1.setClass(mfm.getC(),Editor.class);
                            mfm.startActivity(i1);
                            mfm.finish();
                        }
                        isCloseAc = true;
                    }
                    else {
                        new AlertDialog.Builder(a).setTitle("文件已经存在!").create().show();
                    }
                }
                else if(node == 2){          //删除操作
                    if (f.exists()){
                        fr.del_file(f);
                    }
                    else{
                        new AlertDialog.Builder(a).setTitle("文件夹不存在！").create().show();
                    }
                }

                box.dismiss();

                if(!isCloseAc){
                    if(isChooseDir)
                        new flush_listview(fr, cd.getList_string(), cd.getFile_name_list(), cd.getFile_adapter(), cd.getFile_list());
                    else
                        new flush_listview(fr, mfm.getList_string(), mfm.getFile_name_list(), mfm.getFile_adapter(), mfm.getFile_list());
                }
            }
        });

        //取消按钮
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                box.dismiss();
            }
        });
    }
}
