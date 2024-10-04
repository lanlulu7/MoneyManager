package com.example.final_project_111016046;

import static android.graphics.Color.rgb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class View_PageActicity extends AppCompatActivity
        implements View.OnClickListener, DatePickerDialog.OnDateSetListener,AdapterView.OnItemClickListener{

    static final String db_name="money_manager";
    static final String tb_name="money_managers";

    static final String[] FROM = new String[]{"income_outcome","topic","type","price"};

    SQLiteDatabase db;
    Cursor cur;
    SimpleCursorAdapter adapter;
    ListView lv;
    Calendar c=Calendar.getInstance();
    Calendar calendar=Calendar.getInstance();
    Date date_check=calendar.getTime();
    Date date=c.getTime();
    long date1=c.getTimeInMillis();
    String today=new SimpleDateFormat("yyyy/MM/dd").format(date);

    String find_date;
    String left_Str,right_Str;
    String income="收入";
    String outcome="支出";
    TextView date_view;
    int sum=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page_acticity);

        TextView date_view=(TextView)findViewById(R.id.date_view);
        date_view.setText(today);

        db=openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        String createTable="CREATE TABLE IF NOT EXISTS "+tb_name+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,date varchar(32),income_outcome varchar(32),topic varchar(32),type varchar(32),price varchar(32))";
        db.execSQL(createTable);
        cur=db.rawQuery("SELECT * FROM "+tb_name+" WHERE date = '" + today + "'",null);
        adapter=new SimpleCursorAdapter(
                this,
                R.layout.item,
                cur,
                FROM,
                new int[]{R.id.a_income_outcome,R.id.a_topic,R.id.a_type,R.id.a_amount},
                0
        );
        lv=(ListView)findViewById(R.id.lv);
        lv.setAdapter(adapter);

        Cursor cur1=db.rawQuery("SELECT * FROM "+tb_name, null);
        if(cur1.moveToFirst())
        {
            sum=0;
            do {
                int plus=0,minus=0;
                if(cur1.getString(2).equals(income))
                {
                    String income_plus = cur1.getString(5);
                    plus = Integer.parseInt(income_plus);

                }
                else
                {
                    String outcome_minus = cur1.getString(5);
                    minus = Integer.parseInt(outcome_minus);
                }
                sum=sum+plus-minus;

            }while(cur1.moveToNext());
        }

        TextView left_money = (TextView)findViewById(R.id.left_money);
        left_money.setText("總結餘:"+String.valueOf(sum));

            TextView right_day=(TextView) findViewById(R.id.right_day);
            right_day.setTextColor(rgb(152,159,167));


    }
    private void requery()
    {
        cur=db.rawQuery("SELECT * FROM "+tb_name+" WHERE date = '" + find_date + "'",null);
        adapter.changeCursor(cur);

    }
    public void select(View v)
    {
        DatePickerDialog pick = new DatePickerDialog(this,this,
        c.get(Calendar.YEAR),
        c.get(Calendar.MONTH),
        c.get(Calendar.DAY_OF_MONTH));

        pick.getDatePicker().setMaxDate(date1);
        pick.show();


    }
    public void no(View v)
    {
        finish();
    }

    public void left_click(View v)
    {
            c.add(Calendar.DATE,-1);

            left_Str=new SimpleDateFormat("yyyy/MM/dd").format(c.getTime());
            cur=db.rawQuery("SELECT * FROM "+tb_name+" WHERE date = '" + left_Str + "'",null);
            adapter.changeCursor(cur);
            date_view =(TextView) findViewById(R.id.date_view);
            date_view.setText(left_Str);

            TextView right_day=(TextView) findViewById(R.id.right_day);
            right_day.setTextColor(rgb(255,255,255));

    }
    public void right_click(View v)
    {
        String e = new SimpleDateFormat("yyyy/MM/dd").format(c.getTime());
        String d = new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());


        if(!e.equals(d))
        {
            c.add(Calendar.DATE,1);

            right_Str=new SimpleDateFormat("yyyy/MM/dd").format(c.getTime());
            cur=db.rawQuery("SELECT * FROM "+tb_name+" WHERE date = '" + right_Str + "'",null);
            adapter.changeCursor(cur);
            date_view =(TextView) findViewById(R.id.date_view);
            date_view.setText(right_Str);
        }

        e = new SimpleDateFormat("yyyy/MM/dd").format(c.getTime());
        if(e.equals(d))
        {
            TextView right_day=(TextView) findViewById(R.id.right_day);
            right_day.setTextColor(rgb(152,159,167));
        }



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onDateSet(DatePicker view, int y, int m, int d) {
        date_view =(TextView) findViewById(R.id.date_view);
        if((m+1)<10)
        {
            if(d<10)
            {
                date_view.setText(y+"/"+"0"+(m+1)+"/"+"0"+d);
                find_date=y+"/"+"0"+(m+1)+"/"+"0"+d;
            }
            else
            {
                date_view.setText(y+"/"+"0"+(m+1)+"/"+d);
                find_date=y+"/"+"0"+(m+1)+"/"+d;
            }
        }
        else
        {
            if(d<10)
            {
                date_view.setText(y+"/"+(m+1)+"/"+"0"+d);
                find_date=y+"/"+(m+1)+"/"+"0"+d;
            }
            else
            {
                date_view.setText(y+"/"+(m+1)+"/"+d);
                find_date=y+"/"+(m+1)+"/"+d;
            }
        }

        c.set(Calendar.DATE, d);
        c.set(Calendar.MONTH, m);
        c.set(Calendar.YEAR, y);
        requery();

        String e = new SimpleDateFormat("yyyy/MM/dd").format(c.getTime());
        String s = new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());
        if(e.equals(s))
        {
            TextView right_day=(TextView) findViewById(R.id.right_day);
            right_day.setTextColor(rgb(152,159,167));
        }
        else
        {
            TextView right_day=(TextView) findViewById(R.id.right_day);
            right_day.setTextColor(rgb(255,255,255));
        }
    }
    public boolean onKeyDown(int KeyCode, KeyEvent event)
    {
        if(KeyCode == KeyEvent.KEYCODE_BACK)
        {
            return true;
        }
        return false;
    }
    @Override
    public void onClick(View v) {

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        db.close();
    }
}