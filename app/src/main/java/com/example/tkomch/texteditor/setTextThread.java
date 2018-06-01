package com.example.tkomch.texteditor;

import android.content.Context;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class setTextThread {

    private FileRead fr;
    private Context c;
    private int cache_num;

    public setTextThread (Editor ed){
        fr = ed.getFr();
        c = ed.getC();
    }

    public void run() {
        try {
            cache_num = 1;        //缓存文件号
            char[] cache_file = new char[1000];     //一次读取10000个字符
            //文件流
            FileInputStream input_stream = new FileInputStream(fr.get_read_dir());
            byte[] b = new byte[1024];
            StringBuilder sb = new StringBuilder("");
            int len = 0;
            while ((len = input_stream.read(b)) > 0){
                sb.append(new String(b, 0, len,fr.codeString()));
            }
            input_stream.close();
            //将sb分为各个文件存储
            int sb_length = sb.length();
            int begin = 0;
            int end = begin + 9999;

            if (sb_length <= 10000 && sb_length != 0) {        //如果文件长度小于10000
                FileOutputStream cache = c.openFileOutput("File" + String.valueOf(cache_num), Context.MODE_PRIVATE);
                sb.getChars(0, sb_length - 1, cache_file,0);
                String cache_string = new String(cache_file).trim();
                byte[] write_b = cache_string.getBytes();
                cache.write(write_b);
                cache.close();
            }
            else if(sb_length == 0){
                FileOutputStream cache = c.openFileOutput("File" + String.valueOf(cache_num), Context.MODE_PRIVATE);
                cache.close();
            }
            else {
                //将sb分为不同文件
                while (cache_num <= (sb_length/10000)){
                    FileOutputStream cache = c.openFileOutput("File" + String.valueOf(cache_num++),Context.MODE_PRIVATE);
                    sb.getChars(begin,end,cache_file,0);
                    String cache_string = new String(cache_file).trim();
                    byte[] write_b = cache_string.getBytes();
                    cache.write(write_b);
                    cache.close();
                    begin = end + 1;
                    end = begin + 9999;
                }
                FileOutputStream cache = c.openFileOutput("File" + String.valueOf(cache_num), Context.MODE_PRIVATE);
                sb.getChars(begin, sb_length - 1, cache_file,0);
                String cache_string = new String(cache_file).trim();
                byte[] write_b = cache_string.getBytes();
                cache.write(write_b);
                cache.close();
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getCache_num() {
        return cache_num;
    }
}
