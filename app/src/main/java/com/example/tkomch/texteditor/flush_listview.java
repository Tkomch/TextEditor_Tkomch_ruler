package com.example.tkomch.texteditor;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

//刷新ListView
public class flush_listview {

    //构造函数
    public flush_listview(FileRead fr, List<String> list_string, String[] file_name_list, ArrayAdapter<String> file_adapter, ListView file_list){
        list_string.clear();

        file_name_list = fr.listAllFile();

        file_list.setAdapter(file_adapter);

        for(int i = 0;i < file_name_list.length;i++){
            list_string.add(file_name_list[i]);
        }
    }
}
