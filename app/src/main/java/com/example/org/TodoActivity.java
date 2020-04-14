package com.example.org;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.org.Class.MyListAdapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

public class TodoActivity extends Activity {
    private ListView lv1, lv2, lv4;


    //-----------第三个页面用到的变量-------------------------------------------------------
    private static String DB_NAME = "mydb";
    private EditText et_name;
    private EditText et_age;
    private ArrayList<Map<String, Object>> data;
    private dbHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleAdapter listAdapter;
    private View view;
    private ListView listview;
    private Button selBtn, addBtn, updBtn, delBtn;
    private Map<String, Object> item;
    private String selId, selfn;
    private ContentValues selCV;
    private EditText search;
    private Button dipA;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        {
        /*-----初始化四个切换页面------------------------------------------------------------*/
            // 步骤1：获得TabHost的对象，并进行初始化setup()
            TabHost tabs = (TabHost) findViewById(R.id.tabhost);
            tabs.setup();
            //步骤2：获得TabHost.TabSpec增加tab的一页，通过setContent()增加内容，通过setIndicator增加页的标签

            /*增加第一个Tab */
            TabHost.TabSpec spec = tabs.newTabSpec("Tag1");
            //单击Tab要显示的内容
            lv1 = (ListView) findViewById(R.id.tab1Lv);
            lv1.setAdapter(new MyListAdapter(TodoActivity.this));
            lv1.setItemsCanFocus(true);
            lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(TodoActivity.this, "123123", Toast.LENGTH_SHORT).show();
                }
            });
            spec.setContent(R.id.tab1);
            /* 显示Tab内容*/
            spec.setIndicator("Agenda");
            tabs.addTab(spec);


            /* 增加第二个Tab*/
            lv2 = (ListView) findViewById(R.id.tab2Lv);
            lv2.setAdapter(new MyListAdapter(TodoActivity.this, 2));
            spec = tabs.newTabSpec("Tag2");
            spec.setContent(R.id.tab2);//单击Tab要显示的内容
            /* 显示Tab2内容 */
            spec.setIndicator("TODO");
            tabs.addTab(spec);


            /*增加第三个Tab */
            lv4 = (ListView) findViewById(R.id.tab3Lv);
            lv4.setAdapter(new MyListAdapter(TodoActivity.this, 3));


            /*增加第四个Tab */
            spec = tabs.newTabSpec("Tag4");
            spec.setContent(R.id.tab4);//单击Tab要显示的内容
            /* 显示Tab4内容*/
            spec.setIndicator("DONE");
            tabs.addTab(spec);


            spec = tabs.newTabSpec("Tag3");
            spec.setContent(R.id.tab3);//单击Tab要显示的内容
            /* 显示Tab3内容*/
            spec.setIndicator("FILE");
            tabs.addTab(spec);


            /* 步骤3：可通过setCurrentTab(index)指定显示的页，从0开始计算*/
            tabs.setCurrentTab(0);
        }






        /*-----初始化全局文件页面------------------------------------------------------------*/
        //et_name = (EditText) findViewById(R.id.et_name);
        //et_age = (EditText) findViewById(R.id.et_age);
        //控件定义------------------------------------------------
        listview = (ListView) findViewById(R.id.listView);
        selBtn = (Button) findViewById(R.id.bt_query);
        addBtn = (Button) findViewById(R.id.bt_add);
        updBtn = (Button) findViewById(R.id.bt_modify);
        delBtn = (Button) findViewById(R.id.bt_del);
        dipA = (Button) findViewById(R.id.dispall);
        //搜索框
        search = (EditText)findViewById(R.id.search);


        //设立监听事件------------------------------------------------
        dipA.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(TodoActivity.this,"1",Toast.LENGTH_LONG).show();
                dbFindAll();
            }
        });

        selBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(TodoActivity.this,"1",Toast.LENGTH_LONG).show();
                dbFindsearch();
            }
        });

        addBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dbAdd();
            }
        });

        updBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(isfileexits(selfn)) {
                    Intent intent1;
                    intent1 = new Intent(TodoActivity.this, detailed.class);
                    intent1.putExtra("fn2", selfn);
                    startActivity(intent1);
                }
            }
        });


        delBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dbDel();
                dbFindAll();
            }
        });


        //tab3显示界面初始化------------------------------------------------
        dbHelper = new dbHelper(this, DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();// 打开数据库
        data = new ArrayList<Map<String, Object>>();
        //显示所有文件
        dbFindAll();


        //listview文件显示列表的监听事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // TODO Auto-generated method stub
                //选中的文件位置id
                Map<String, Object> listItem = (Map<String, Object>) listview.getItemAtPosition(position);
                //et_name.setText((String) listItem.get("name"));
                //et_age.setText((String) listItem.get("age"));
                selId = (String) listItem.get("_id");
                selfn = (String) listItem.get("filename");

                Log.i("mydbDemo", "id = " + selId);
                Log.i("mydbDemo", "filename = " + selfn);
            }
        });
    }





    //数据删除=================================================================
    protected void dbDel() {
        // TODO Auto-generated method stub
        //删除文件
        try {
            File ft = new File("/data/data/com.example.org/files/"+selfn);
            ft.delete();
            Toast.makeText(this,"删除成功", LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(this,"删除失败", LENGTH_LONG).show();
            e.printStackTrace();
        }


        //删除数据库中对应条目
        String where = "_id=" + selId;
        int i = db.delete(dbHelper.TB_NAME, where, null);
        if (i > 0)
            Log.i("myDbDemo", "数据库数据删除成功!");
        else
            Log.i("myDbDemo", "数据库数据未删除!");
    }
    /*
    //更新列表中的数据===========================================================
    protected void dbUpdate() {
        // TODO Auto-generated method stub
        ContentValues values = new ContentValues();
        //values.put("name", et_name.getText().toString().trim());
        //values.put("age", et_age.getText().toString().trim());
        String where = "_id=" + selId;
        int i = db.update(dbHelper.TB_NAME, values, where, null);
        if (i > 0)
            Log.i("myDbDemo", "数据更新成功！");
        else
            Log.i("myDbDemo", "数据未更新");
    }
    */
    //插入数据
    protected void dbAdd() {
        Intent intent;
        intent = new Intent(TodoActivity.this, MainActivity .class);
        startActivity(intent);
    }


    //显示所有数据
    protected void dbFindAll() {
        // TODO Auto-generated method stub
        data.clear();
        cursor = db.query(dbHelper.TB_NAME, null, null, null, null, null, "_id ASC");
        cursor.moveToFirst();
        /*
        try {
            File f = new File("/data/data/com.example.org/files/");    //建立File变量,并设定由f变量变数引用

            String fileList[] = f.list();    //调用不带参数的list()方法
            for (int i = 0; i < fileList.length; i++) {    //遍历返回的字符数组
                String id = String.valueOf(i);
                String fn=null, md=null;
                try{
                    //定义文件类
                    File curfile = new File("/data/data/com.example.org/files/"+fileList[i]);
                    //得到文件名
                    fn = curfile.getName();
                    //获取文件最后修改日期
                    long time = curfile.lastModified();
                    Date date = new Date(time);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    md = formatter.format(date);//格式化时间
                }
                catch (Exception e){
                    Toast.makeText(TodoActivity.this,"failed finding",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


                item = new HashMap<String, Object>();
                item.put("_id", id);
                item.put("filename", fn);
                item.put("lastmordified", md);
                data.add(item);
                //Toast.makeText(TodoActivity.this, fileList[i], Toast.LENGTH_LONG).show();
            }
            Toast.makeText(TodoActivity.this,"find successfully",Toast.LENGTH_LONG).show();
        }
        catch(Exception e){
            Toast.makeText(TodoActivity.this,"failed finding",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        */

        while (!cursor.isAfterLast()) {
            String id = cursor.getString(0);
            String fn = cursor.getString(1);
            String md = cursor.getString(2);
            item = new HashMap<String, Object>();
            item.put("_id", id);
            item.put("filename", fn);
            item.put("lastmordified", md);
            data.add(item);
            cursor.moveToNext();
        }

        showList();
    }



    //显示搜索数据
    protected void dbFindsearch() {
        // TODO Auto-generated method stub
        data.clear();
        cursor = db.query(dbHelper.TB_NAME, null, null, null, null, null, "_id ASC");
        //cursor = db.rawQuery("select * from mydb where filename = "+search.getText().toString(), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(0);
            String fn = cursor.getString(1);
            String md = cursor.getString(2);
            item = new HashMap<String, Object>();
            item.put("_id", id);
            item.put("filename", fn);
            item.put("lastmordified", md);
            Log.i("mydbDemo", "fn = " + fn);
            Log.i("mydbDemo", "search = " + search.getText().toString());
            Log.i("mydbDemo", String.valueOf(fn.equals(search.getText().toString())));
            if(fn.equals(search.getText().toString())){
                data.add(item);
            }
            cursor.moveToNext();
        }

        showList();
    }



    //供Findall调用的显示列表函数
    private void showList() {
        // TODO Auto-generated method stub
        listAdapter = new SimpleAdapter(this, data,
                R.layout.listfiletab3, new String[]{"_id", "filename", "lastmordified"}, new int[]{R.id.ID, R.id.fn, R.id.lmd});
        listview.setAdapter(listAdapter);
    }

    private boolean isfileexits(String fnn){
        try {
            File f = new File("/data/data/com.example.org/files/");    //建立File变量,并设定由f变量变数引用

            String fileList[] = f.list();    //调用不带参数的list()方法
            for (int i = 0; i < fileList.length; i++) {    //遍历返回的字符数组
                if(fnn.equals(fileList[i])){
                    return true;
                }
            }
        }
        catch(Exception e){
            //Toast.makeText(TodoActivity.this,"failed finding",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return false;
    }

}