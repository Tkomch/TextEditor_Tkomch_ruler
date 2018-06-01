package com.example.tkomch.texteditor;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileRead {

    private String read_dir = null;                 //储存要读的文件路径
    public File sd_dir = Environment.getExternalStorageDirectory();                     //存储的默认路径

    //当传入参数路径时
    public FileRead(String path,Activity ac){
        GetRMPermison(ac);
        read_dir = path;
    }
    //没有传入参数路径
    public FileRead(Activity ac){
        GetRMPermison(ac);
        read_dir =  sd_dir + File.separator;
    }

    //列出所有read_dir下的文件
    public String[] listAllFile(){
        String[] file_list = new File(read_dir).list();
        File[] filetype_list = new File(read_dir).listFiles();
        for(int i = 0;i < filetype_list.length;i++){
            if(isDir(filetype_list[i])){
                file_list[i] += "\ndir";
            }
            else{
                file_list[i] += "\nfile";
            }
        }
        return file_list;
    }

    //得到当前的路径
    public String get_read_dir(){
        return read_dir;
    }

    //递归删除文件及文件夹
    public void del_file(File file){
        if(isDir(file)){
            File[] files = file.listFiles();
            for(int i = 0;i < files.length;i++){
                del_file(files[i]);
            }
        }
        file.delete();
    }

    public Boolean isDir(File f){
        return f.isDirectory();
    }

    //自动获取文件编码
    public String codeString() throws FileNotFoundException,IOException{
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(read_dir));
        int p = (bin.read() << 8) + bin.read();
        String code = null;

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
                break;
        }
        return code;
    }

    public void GetRMPermison(Activity ac) {
        ActivityCompat.requestPermissions(
                ac,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                1
        );
    }
}