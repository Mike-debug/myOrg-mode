package com.example.org.Class;
import com.example.org.MainActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.*;
import java.util.*;

import androidx.core.content.ContextCompat;

import com.example.org.R;
import com.example.org.dbHelper;

import java.io.File;

public class MyListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflator;

    //ListView的数据域
    private int count = filenumber();
    private int[] state;
    private int[] tag;
    private String[] title;
    private String[] note;
    private String[] deadline;


    private MainActivity m = new MainActivity();
    //与文件同步
    //文件目录
    private static File fp = new File("/data/data/com.example.org/files");


    private static int filenumber(){
        String fileList[] = fp.list();    //调用不带参数的list()方法
        return fileList.length;
    }
    String[] FileNameList(){
        return fp.list();    //调用不带参数的list()方法
    }
    public String[] FileAttr(String Fn)
    {
        FileReader fr=null;
        StringBuilder a = new StringBuilder("");
        ArrayList<String> back= new ArrayList();
        String[] b1;
        String[] b2;
        String[] b3 = new String[0];
        try
        {
            Log.i("yes1:","yes1");
            fr=new FileReader("/data/data/com.example.org/files/"+Fn);    //创建FileReader对象
            int i=0;
            while((i=fr.read())!=-1)
            {    //循环读取
                a.append((char)i);
                //System.out.print((char) i);    //将读取的内容强制转换为char类型
            }
            Log.i("yes2:","yes2");
            StringReader sr = new StringReader(a.toString());
            BufferedReader br = new BufferedReader(sr);
            b1=br.readLine().split(" ");
            for(i = 0;i<b1.length;++i) {
                if(i==1) {
                    b1[i]=b1[i].substring(b1[i].indexOf("#")+1,b1[i].lastIndexOf("]"));
                }
                back.add(b1[i]);
            }
            br.readLine();
            String tt = br.readLine();
            back.add(tt.substring(tt.indexOf('<')+1, tt.indexOf('>')));
            b2 = new String[back.size()];
            for(i = 0;i<b2.length;++i) {
                b2[i]=(String)back.toArray()[i];
            }
            b3 = b2;
            Log.i("yes:","yes");
            return b2;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Log.i("no:","no");
        return b3;
    }


    public MyListAdapter(Context context){
        this.mContext =  context;
        mLayoutInflator = LayoutInflater.from(context);

        getInfo(1);
    }

    public MyListAdapter(Context context, int mode){ //1 全部 2 todo 3 done
        this.mContext =  context;
        mLayoutInflator = LayoutInflater.from(context);

        getInfo(mode);
    }


    //------------------------------------------------------------------------在此读入数据，主要是count，和count的循环
    public void getInfo(int mode){

            count = filenumber();
            if(count > 0){
                state = new int[count];
                tag = new int[count];
                title = new String[count];
                note = new String[count];
                deadline = new String[count];
                for(int i = 0; i < count; i++ ){
                    /*
                    state[i] = 1; //1 UNDO, 2 DONE, 3 NONE
                    tag[i] = 1; //1 A 2 B 3 C
                    title[i] = new String("我是第" + String.valueOf(i+1) + "个title");
                    note[i] = new String("我是第" + String.valueOf(i+1) + "个note");
                    */
                    Log.i("a1:","a1");
                    String [] At = FileAttr(fp.list()[i]);
                    for(int j = 0;j<At.length;++j){
                        Log.i("at:","!");
                    }
                    state[i] = At[0].equals("Todo")?1:(At[0].equals("Done")?2:3);
                    tag[i] = At[1].equals("A")?1:(At[5].equals("B")?2:3);
                    title[i]= At[2];
                    note[i]=At[3];
                    deadline[i]=At[4];
                }
                //note[0] = "太长最后显示三个点We will talk about how to use Android Studio.";
                //title[0] = ("标题如果太长可以跑马灯跑起来呀呀呀呀呀呀快跑啊安安啊啊啊啊啊啊啊");
                //state[2] = 2;
                //state[3] = 2;
                //tag[3] = 2;
                //tag[6] = 3;
            }


            if(mode == 2)
            {
                int sum = count;
                count = 0;
                for (int i=0; i < sum; i++)
                {
                    if(state[i] == 1 || state[i] == 3)
                    {
                        state[count] = state[i];
                        tag[count] = tag[i];
                        title[count] = title[i];
                        note[count] = note[i];
                        count++;
                    }
                }
            }


            else if(mode == 3)
            {
                int sum = count;
                count = 0;
                for (int i=0; i < sum; i++)
                {
                    if(state[i] == 2)
                    {
                        state[count] = state[i];
                        tag[count] = tag[i];
                        title[count] = title[i];
                        note[count] = note[i];
                        count++;
                    }

                }
            }


    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder{
        public ImageView state,tag;
        public TextView time, title, note;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {  //position代表这是第几个列表，下标从0开始
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mLayoutInflator.inflate(R.layout.layout_list_item, null);
            holder = new ViewHolder();
            holder.state = (ImageView) convertView.findViewById(R.id.ivstate);
            holder.tag = (ImageView) convertView.findViewById(R.id.ivtag);
            holder.note = (TextView) convertView.findViewById(R.id.tvnote);
            holder.time = (TextView) convertView.findViewById(R.id.tvtime);
            holder.title = (TextView) convertView.findViewById(R.id.tvtitle);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        //给控件赋值
        holder.time.setText(deadline[position]);
        holder.note.setText(note[position]);
        holder.title.setSelected(true);
        holder.title.setText(title[position]);

        if(state[position] == 2)
        {
            holder.title.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.note.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        //图片显示
        if(state[position] == 2) {
            holder.state.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(),R.drawable.done));
        } else
        {
            holder.state.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(),R.drawable.wait));
        }

        if(tag[position] == 1) {
            holder.tag.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(),R.drawable.priority_a));
        } else if(tag[position] == 2)
        {
            holder.tag.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(),R.drawable.priority_b));
        } else{
            holder.tag.setImageDrawable(ContextCompat.getDrawable(mContext.getApplicationContext(),R.drawable.priority_c));
        }


        return convertView;
    }


}
