package com.example.tkomch.texteditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Editor extends AppCompatActivity {

    private Button pageup;
    private Button pagedown;
    private Button qiehuan;
    private Button back;
    private Button save;
    private EditText editor;
    private TextView info;
    private boolean editor_enable = false;
    private Activity ac = Editor.this;
    private String file_path;
    private FileRead fr;
    private setTextThread st;
    private Thread th;
    private Context c = this;
    private int  n;            //当前的页数
    //private int n_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        pageup = (Button) findViewById(R.id.search);    //上一页按钮
        pagedown = (Button) findViewById(R.id.change);    //下一页按钮
        qiehuan = (Button) findViewById(R.id.qiehuan);  //切换状态按钮
        back = (Button) findViewById(R.id.back);        //返回按钮
        save = (Button) findViewById(R.id.save);        //保存按钮
        editor = (EditText) findViewById(R.id.editor);    //编辑器
        info = (TextView) findViewById(R.id.disinfo);


        Intent i = getIntent();
        Bundle bd = i.getExtras();
        file_path = bd.getString("openfile");

        fr = new FileRead(file_path,ac);

        editor.setFocusable(false);
        editor.setFocusableInTouchMode(false);
        editor_enable = false;

        st = new setTextThread(this);
        st.run();

        //n_sum = st.getCache_num();
        //n_sum=6;

        String[] file_name = c.fileList();
        n = 1;
        final int file_sum = getFileNum();

        StringBuilder s = new StringBuilder();
        for(int it = 0;it < c.fileList().length;it++){
            s.append(c.fileList()[it] + " ");
        }

        info.setText(s + String.valueOf(getFileNum()));

        //读取缓存文件
        try {
            FileInputStream read_s = c.openFileInput("File" + String.valueOf(n));
            byte[] b = new byte[1024];
            StringBuilder sb = new StringBuilder("");
            int len = 0;
            while ((len = read_s.read(b)) > 0) {
                sb.append(new String(b, 0, len,fr.codeString()));
            }
            read_s.close();
            editor.setText(sb);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        //切换编辑模式与非编辑模式
        qiehuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editor_enable){
                    editor.setEnabled(true);
                    editor.setFocusable(true);
                    editor.setFocusableInTouchMode(true);
                    editor_enable = true;
                    new AlertDialog.Builder(ac).setTitle("OK").create().show();
                }
                else {
                    editor.setFocusable(false);
                    editor.setFocusableInTouchMode(false);
                    editor_enable = false;
                    new AlertDialog.Builder(ac).setTitle("NO").create().show();
                }
            }
        });

        //返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //清空缓存 : true
                int file_num = 0;
                while(file_num < file_sum){
                    //c.deleteFile("File5");
                    c.deleteFile("File" + String.valueOf(file_num + 1));
                    file_num++;
                }
                finish();
            }
        });

        //上一页按钮
        pageup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //将文件写入缓存
                    FileOutputStream filecache_out = c.openFileOutput("File" + String.valueOf(n),MODE_PRIVATE);
                    String string_file = editor.getText().toString();
                    byte[] b_cache = string_file.getBytes();
                    filecache_out.write(b_cache);
                    filecache_out.close();


                    if (isHere("File" + String.valueOf(n - 1))) {
                        FileInputStream read_s = c.openFileInput("File" + String.valueOf(n - 1));
                        byte[] b = new byte[1024];
                        StringBuilder sb = new StringBuilder("");
                        int len = 0;
                        while ((len = read_s.read(b)) > 0) {
                            sb.append(new String(b, 0, len, fr.codeString()));
                        }
                        read_s.close();
                        editor.setText(sb);
                        n--;
                    }
                    else {
                        new AlertDialog.Builder(ac).setTitle("没有上一页了!折返吧！").create().show();
                    }
                    info.setText(fr.get_read_dir() + ":" + String.valueOf(n));
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        //下一页按钮
        pagedown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //将文件写入缓存
                    FileOutputStream filecache_out = c.openFileOutput("File" + String.valueOf(n),MODE_PRIVATE);
                    String string_file = editor.getText().toString();
                    byte[] b_cache = string_file.getBytes();
                    filecache_out.write(b_cache);
                    filecache_out.close();

                    //info.setText(string_file);

                    if (isHere("File" + String.valueOf(n + 1))) {
                        FileInputStream read_s = c.openFileInput("File" + String.valueOf(n + 1));
                        byte[] b = new byte[1024];
                        StringBuilder sb = new StringBuilder("");
                        int len = 0;
                        while ((len = read_s.read(b)) > 0) {
                            sb.append(new String(b, 0, len, fr.codeString()));
                        }
                        read_s.close();
                        editor.setText(sb);
                        n++;
                    }
                    else {
                        new AlertDialog.Builder(ac).setTitle("没有下一页了!折返吧！").create().show();
                    }
//                    info.setText(editor.getText().toString());
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        });

        //保存按钮
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存文件操作
                try {
                    //写入缓存的文件
                    //info.setText(String.valueOf(n));
                    FileOutputStream filecache_out = c.openFileOutput("File" + String.valueOf(n), MODE_PRIVATE);
                    String string_file = editor.getText().toString().trim();
                    byte[] b_cache = string_file.getBytes();
                    filecache_out.write(b_cache);
                    filecache_out.close();

                    info.setText(String.valueOf(string_file.length()));

                    //将缓存的文件内容读取出来
                    StringBuilder string_builder = new StringBuilder();
                    for (int i = 1; i <= getFileNum(); i++) {
                        FileInputStream file_in = c.openFileInput("File" + String.valueOf(i));
                        BufferedReader buffer_reader = new BufferedReader(new InputStreamReader(file_in));
                        String line_string = "";
                        while ((line_string = buffer_reader.readLine()) != null) {
                            string_builder.append(line_string.trim() + "\n");
                        }
                        file_in.close();
                    }

                    //editor.setText(string_builder);
                    //info.setText(String.valueOf(string_builder.length()));

                    //将读取出来的内容写入文件
                    FileOutputStream file_out = new FileOutputStream(fr.get_read_dir());
                    byte[] b_file = string_builder.toString().getBytes();
                    file_out.write(b_file);
                    file_out.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }

                new AlertDialog.Builder(ac).setTitle("保存成功!").create().show();

                //int file_num = 0;
                //while(file_num < c.fileList().length){
                    //c.deleteFile("File2");
                    //c.deleteFile("File" + String.valueOf(file_num + 1));
                //    c.deleteFile(c.fileList()[file_num]);
                //    file_num++;
                //}
            }
        });
    }

    //判断缓存目录是否有该文件
    public boolean isHere(String filename){
        String[] file_name = c.fileList();
        boolean is_here = false;
        for(int i = 0;i < file_name.length;i++) {
            if (filename.equals(file_name[i]))
                is_here = true;
        }
        return is_here;
    }

    //显示缓存File文件个数
    public int getFileNum(){
        String[] file_names = c.fileList();
        int file_num = 0;
        for (int i = 0;i < file_names.length;i++){
            //判断文件是否有File
            if(file_names[i].split("e")[0].trim().equals("Fil"))
                file_num++;
        }
        return file_num;
    }

    public EditText getEditor() {
        return editor;
    }

    public FileRead getFr() {
        return fr;
    }

    public Context getC() {
        return c;
    }
}
