package com.example.org;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class detailed extends AppCompatActivity{
    //两个空间，文本框和按钮
    private TextView fc;
    private Button mb;
    private Button bk;
    //接受传输的数据
    private String fn2;

    //用于显示和操作的文件类
    private File fp;
    private RandomAccessFile raf;
    private int len;
    private byte[] buffer=new byte[2];
    private String t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        //初始化定义
        fc = (TextView) this.findViewById(R.id.filecontent);
        mb = (Button) this.findViewById(R.id.mordify);
        bk = (Button) this.findViewById(R.id.back1);
        Intent intent =  getIntent();
        fn2 = intent.getStringExtra("fn2");
        fp = new File("/data/data/com.example.org/files/"+fn2);


        fc.setText(null);
        String get = "";
        try {
            FileInputStream fis = openFileInput(fn2);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            get = new String(buffer);
            fc.setText(get);
            //Toast.makeText(detailed.this, "保存的数据是\n"+ get, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }



        mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1;
                intent1 = new Intent(detailed.this, MainActivity.class);
                intent1.putExtra("fn2", fn2);
                startActivity(intent1);
            }
        });


        bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2;
                intent2 = new Intent(detailed.this, TodoActivity.class);
                startActivity(intent2);
            }
        });

    }
}
