package com.example.org;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtDate;
    private Button btnDate, enddate, reminddate;
    DateFormat format= DateFormat.getDateTimeInstance();
    Calendar calendar= Calendar.getInstance(Locale.CHINA);
    private EditText SaveText;
    private Button SaveButton, LoadButton;
    private Button statebtn, prioritybtn, tagbtn;
    private static String DB_NAME = "mydb";
    private EditText Headline, title;


    //用于插入数据的数据库
    private SQLiteDatabase db;
    public dbHelper dbHelper;
    private Cursor cursor;

    //确定按钮
    private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder)
    {
        return builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "您单击了[确定]钮！",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //取消按钮
    private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder)
    {
        return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "您单击了[取消]按钮！",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //接受文件名
    private String fn2;
    private Button btnNotes;
    //主体，初始化界面和动作定义
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnNotes = (Button) findViewById(R.id.notes);
        btnNotes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(SaveText.getVisibility() == View.VISIBLE)
                    SaveText.setVisibility(View.INVISIBLE);
                else
                    SaveText.setVisibility(View.VISIBLE);
            }
        });

        Headline=(EditText)findViewById(R.id.title);
        title = (EditText)findViewById(R.id.filename);
        //删除多余文件
        /*
        File t = new File("/data/data/com.example.org/files/null.org");
        t.delete();

        try {
            deleteFile("/data/data/com.example.org/files/.org");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        */

        //初始化数据库
        dbHelper = new dbHelper(this, DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();// 打开数据库


        Intent intent =  getIntent();
        fn2 = intent.getStringExtra("fn2");
        if(isfileexits(fn2)){
            title.setText(fn2);
        }




        //取消，返回主界面
        Button Cancelbtn = (Button)this.findViewById(R.id.CancelBtn);
        Cancelbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent;
                intent = new Intent(MainActivity.this, TodoActivity .class);
                startActivity(intent);
            }
        });

        //--------------------------选择状态----------------------------------------------------
        statebtn = (Button)this.findViewById(R.id.statebtn);
        statebtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                //Intent intent = new Intent(MainActivity.this, TodoActivity.class);
                //startActivity(intent);
                final String[] state = new String[]{"None", "Todo", "Done"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                setNegativeButton(builder);//添加“取消”按钮
                setTitle("Set Action");
                builder.setItems(state, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "您选择了"+state[which], Toast.LENGTH_SHORT).show();
                        statebtn.setText(state[which]);
                    }
                });

                builder.create().show();
            }

        });


        //--------------------------选择优先级--------------------------------------------------
        prioritybtn = (Button)this.findViewById(R.id.prioritybtn);
        prioritybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] Prior = {"None","A","B","C"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                setNegativeButton(builder);//添加“取消”按钮
                setTitle("Set Action");
                builder.setItems(Prior, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "您选择了"+Prior[which], Toast.LENGTH_SHORT).show();
                        prioritybtn.setText(Prior[which]);
                    }
                });

                builder.create().show();
            }
        });



        //--------------------------选择标签----------------------------------------------------
        tagbtn = (Button)this.findViewById(R.id.tagbtn);
        final Button addtag = (Button)this.findViewById(R.id.add);
        //final ArrayList<String> tags = null;
        //tags.add(0,"temp");
        //tags.add(1,"longtime");
        //ArrayList<String> tags = null;
        //tags.add("temp");
        //tags.add("longtime");

        addtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tagbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                setNegativeButton(builder);//添加“取消”按钮
                setTitle("Set Action");
                //final String[] tt = EtSconvert(tags);
                final String[] tt = {"temp","longtime","study","entertainment","sports","meeting"};
                builder.setItems(tt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "您选择了"+tt[which], Toast.LENGTH_SHORT).show();
                        tagbtn.setText(tt[which]);
                    }
                });
                builder.create().show();
            }
        });



        //--------------------------选择日期----------------------------------------------------
        btnDate= (Button) findViewById(R.id.startbtn);
        txtDate= (TextView) findViewById(R.id.startbtn);
        btnDate.setOnClickListener(this);

        enddate = (Button) this.findViewById(R.id.endbtn);
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(MainActivity.this ,  2, enddate, calendar);
            }
        });

        reminddate = (Button) this.findViewById(R.id.remindbtn);
        reminddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(MainActivity.this ,  2, reminddate, calendar);
            }
        });



        SaveText = (EditText) findViewById(R.id.note);
        SaveButton = (Button) findViewById(R.id.SaveBtn);
        LoadButton = (Button) findViewById(R.id.edit);
        SaveButton.setOnClickListener(new ButtonListener());
        LoadButton.setOnClickListener(new ButtonListener());
    }

    //一个分离的动作定义
    public void onClick(View view){
        if(view.getId()==R.id.startbtn){
            showDatePickerDialog(this, 2, txtDate, calendar);
        }
    }

    /**
     * 日期选择
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    public static void showDatePickerDialog(Activity activity, int themeResId, final TextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity, themeResId, new DatePickerDialog.OnDateSetListener() {
            // 绑定监听器(How the parent is notified that the date is set.)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 此处得到选择的时间，可以进行你想要的操作
                tv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            }
        }
        // 设置初始日期
        , calendar.get(Calendar.YEAR)
        , calendar.get(Calendar.MONTH)
        , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public final static String[] EtSconvert(ArrayList<String> arrayList){
        ArrayList<String> temp = new ArrayList<String>(arrayList);
        int size = arrayList.size();
        for(int i = 0; i < size; ++i){
            arrayList.set(i, arrayList.get(i)+";");
        }
        final String[] Sarray = arrayList.toString().split(";");
        return Sarray;
    }

//---------------保存内容------------------------------------------------------------------------------



    private class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //保存数据
                case R.id.SaveBtn:

                    if(isfileexits(title.getText().toString()+".org")){
                        cursor = db.query(dbHelper.TB_NAME, null, null, null, null, null, "_id ASC");
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            if(cursor.getString(1).equals(title.getText().toString()+".org")){
                                break;
                            }
                            cursor.moveToNext();
                            //Log.i("cmp",cursor.getString(1)+" || "+title.getText().toString()+".org");
                        }
                        if(!cursor.isAfterLast()){
                            dbDel();
                            Log.i("did","did");
                        }
                        else{
                            Log.i("non","non1");
                        }
                    }
                    else{
                        Log.i("non","non2");
                        Log.i("non",title.getText().toString()+".org");

                    }
                    //首先保存文件
                    //保存的内容
                    //存储数据的格式
                    String saveinfo =
                            statebtn.getText().toString().trim()+" "
                                +"[#"+ prioritybtn.getText().toString().trim()+"] "
                                    +Headline.getText().toString().trim()+" "
                                    + tagbtn.getText().toString().trim()+"\n"
                                    +"DEADLINE:<"+ btnDate.getText().toString().trim()+">\n"
                                    +"SCHEDULED<"+ enddate.getText().toString().trim()+">\n"
                                    +"<"+ reminddate.getText().toString().trim()+">\n"
                                    + SaveText.getText().toString().trim();
                    FileOutputStream fos;
                    try {
                        fos = openFileOutput(title.getText().toString().trim()+".org", MODE_APPEND);
                        //fos = openFileOutput("test", MODE_APPEND);
                        fos.write(saveinfo.getBytes());
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //然后更新数据库
                    try {
                        //Toast.makeText(MainActivity.this,"save successfully",Toast.LENGTH_LONG).show();
                        File f = new File("/data/data/com.example.org/files/"+title.getText()+".org");    //建立File变量,并设定由f变量变数引用
                        insert(f);

                    }
                    catch(Exception e){
                        Toast.makeText(MainActivity.this,"Insert to db failed",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    //提示数据保存成功
                    Toast.makeText(MainActivity.this, "数据保存成功", Toast.LENGTH_LONG).show();
                    break;

                //读取数据
                case R.id.edit:
                    String get = "";
                    try {
                        FileInputStream fis = openFileInput(title.getText().toString().trim()+".org");
                        //FileInputStream fis = openFileInput("test");
                        byte[] buffer = new byte[fis.available()];
                        fis.read(buffer);
                        get = new String(buffer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(MainActivity.this, "保存的数据是\n"
                            + get, Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;

            }
        }
    }

    protected void insert(File tmp) throws FileNotFoundException {
        // TODO Auto-generated method stub

        ContentValues values = new ContentValues();

        //1插入文件名
        values.put("filename", tmp.getName().trim());
        //2插入最后修改日期
        long time = tmp.lastModified();
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String md = formatter.format(date);//格式化时间
        values.put("date", md.trim());

        //插入其他数据
        FileInputStream ft = openFileInput(tmp.getName().trim());
        values.put("state",statebtn.getText().toString().trim());
        values.put("priority",prioritybtn.getText().toString().trim());
        EditText headline = this.findViewById(R.id.title);
        values.put("headline",headline.getText().toString().trim());
        values.put("tags",tagbtn.getText().toString().trim());
        //values.put("sc",btnDate.getText().toString().trim());
        //values.put("de",enddate.getText().toString().trim());
        //values.put("re",reminddate.getText().toString().trim());
        //values.put("content",SaveText.getText().toString().trim());
        long rowid = db.insert(dbHelper.TB_NAME, null, values);
        if (rowid == -1)
            Log.i("myDbDemo", "数据插入失败！");
        else
            Log.i("myDbDemo", "数据插入成功!" + rowid);
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

    protected void dbDel() {
        // TODO Auto-generated method stub
        //删除文件
        try {
            File ft = new File("/data/data/com.example.org/files/"+cursor.getString(1));
            ft.delete();
            Log.i("m","删除成功");
            //Toast.makeText(this,"删除成功", LENGTH_LONG).show();
        }
        catch (Exception e){
            //Toast.makeText(this,"删除失败", LENGTH_LONG).show();
            e.printStackTrace();
            Log.i("m","删除失败");
        }


        //删除数据库中对应条目
        String where = "_id=" + cursor.getString(0);
        int i = db.delete(dbHelper.TB_NAME, where, null);
        if (i > 0)
            Log.i("myDbDemo", "数据库数据删除成功!");
        else
            Log.i("myDbDemo", "数据库数据未删除!");
    }
}