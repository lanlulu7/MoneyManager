package com.example.final_project_111016046;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class Add_PageActivity extends AppCompatActivity
    implements View.OnClickListener, DatePickerDialog.OnDateSetListener , AdapterView.OnItemSelectedListener {

    Calendar c = Calendar.getInstance();
    long date1=c.getTimeInMillis();
    TextView txDate;

    Spinner money,category;
    String[] category1={"","薪水","理財","兼職","禮金","其他"};
    String[] category2={"","購物","食物","娛樂","日常用品","交通","醫療","其他"};

    static final String db_name="money_manager";
    static final String tb_name="money_managers";

    static final String[] FROM = new String[]{"_id","date","income_outcome","topic","type","price","left_money"};

    SQLiteDatabase db;
    EditText theme,amount;
    String dateStr="";
    Intent it_fix;
    int ID,check_id=0;
    String fix_year,fix_month,fix_day;
    String fix_date,fix_topic,fix_income_outcome,fix_type,fix_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_page);

        it_fix = getIntent();
        ID = it_fix.getIntExtra("id",-1);

        txDate = (TextView)findViewById(R.id.txDate);
        txDate.setOnClickListener(this);

        money=(Spinner)findViewById(R.id.money);
        category=(Spinner)findViewById(R.id.category);

        money.setOnItemSelectedListener(this);
        category.setOnItemSelectedListener(this);

        theme =(EditText) findViewById(R.id.theme);

        amount = (EditText) findViewById(R.id.amount);


        db=openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        String createTable="CREATE TABLE IF NOT EXISTS "+tb_name+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,date varchar(32),income_outcome varchar(32),topic varchar(32),type varchar(32),price varchar(32))";

        db.execSQL(createTable);

        if(ID!=-1)
        {
            fix_date = it_fix.getStringExtra("日期");
            txDate.setText(fix_date);
            dateStr=fix_date;

            String part[]=fix_date.split("/");
            fix_year = part[0];
            fix_month = part[1];
            fix_day = part[2];

            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(fix_day));
            c.set(Calendar.MONTH, Integer.parseInt(fix_month)-1);
            c.set(Calendar.YEAR, Integer.parseInt(fix_year));

            fix_income_outcome = it_fix.getStringExtra("收支出");
            fix_topic = it_fix.getStringExtra("主題");
            theme.setText(fix_topic);

            fix_price = it_fix.getStringExtra("金額");
            amount.setText(fix_price);

            if(fix_income_outcome.equals("收入"))
            {
                money.setSelection(1);
            }
            else
            {
                money.setSelection(2);
            }
            check_id=1;
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
    public void onDateSet(DatePicker view, int y, int m, int d)
    {
        c.set(Calendar.DAY_OF_MONTH, d);
        c.set(Calendar.MONTH, m);
        c.set(Calendar.YEAR, y);
        txDate.setText("日期:"+y+"/"+(m+1)+"/"+d);
        dateStr=y+"/"+(m+1)+"/"+d;
        if((m+1)<10)
        {
            if(d<10)
            {
                txDate.setText(y+"/"+"0"+(m+1)+"/"+"0"+d);
                dateStr=y+"/"+"0"+(m+1)+"/"+"0"+d;
            }
            else
            {
                txDate.setText(y+"/"+"0"+(m+1)+"/"+d);
                dateStr=y+"/"+"0"+(m+1)+"/"+d;
            }
        }
        else
        {
            if(d<10)
            {
                txDate.setText(y+"/"+(m+1)+"/"+"0"+d);
                dateStr=y+"/"+(m+1)+"/"+"0"+d;
            }
            else
            {
                txDate.setText(y+"/"+(m+1)+"/"+d);
                dateStr=y+"/"+(m+1)+"/"+d;
            }
        }

    }

    @Override
    public void onClick(View v)
    {
        if(v==txDate)
        {
            DatePickerDialog pick = new DatePickerDialog(this,this,
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
            pick.getDatePicker().setMaxDate(date1);
            pick.show();
        }
    }
    public void yes(View v)
    {
        if(ID==-1)
        {
            String themeStr=theme.getText().toString().trim();
            String amountStr=amount.getText().toString().trim();
            String income_outcomeStr=(String) money.getSelectedItem();
            String typeStr=(String)category.getSelectedItem();
            if(!dateStr.equals("") && themeStr.length()!=0 && amountStr.length()!=0 && !typeStr.equals("") && income_outcomeStr.length()!=3)
            {

                ContentValues cv =new ContentValues(5);
                cv.put("topic",themeStr);
                cv.put("price",amountStr);
                cv.put("income_outcome",income_outcomeStr);
                cv.put("type",typeStr);
                cv.put("date",dateStr);
                db.insert(tb_name,null,cv);
                dateStr="";
                Intent it = new Intent(this, Main_PageActivity.class);
                startActivity(it);
                finish();
            }
            else
            {
                Toast tos=Toast.makeText(this,"請輸入完整資訊", Toast.LENGTH_SHORT);
                tos.show();
            }
        }
        else
        {
            String themeStr=theme.getText().toString().trim();
            String amountStr=amount.getText().toString().trim();
            String income_outcomeStr=(String) money.getSelectedItem();
            String typeStr=(String)category.getSelectedItem();
            if(!dateStr.equals("") && themeStr.length()!=0 && amountStr.length()!=0 && !typeStr.equals("") && income_outcomeStr.length()!=3)
            {

                ContentValues cv =new ContentValues(5);
                cv.put("topic",themeStr);
                cv.put("price",amountStr);
                cv.put("income_outcome",income_outcomeStr);
                cv.put("type",typeStr);
                cv.put("date",dateStr);
                db.update(tb_name,cv, "_id="+ID, null);
                dateStr="";
                Intent it = new Intent(this, Main_PageActivity.class);
                startActivity(it);
                finish();
            }
            else
            {
                Toast tos=Toast.makeText(this,"請輸入完整資訊", Toast.LENGTH_SHORT);
                tos.show();
            }
        }



    }
    public void no(View v)
    {
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if(parent.getId() == R.id.category)
        {
            Typeface tf = getResources().getFont(R.font.baboo);
            TextView child = (TextView) parent.getChildAt(0);
            child.setTextSize(26);
            child.setTypeface(tf);
            return;
        }


        String[] item={};
        if(position==1)
        {
            item=category1;
        }
        if(position==2)
        {
            item=category2;
        }
        ArrayAdapter<String> category_ad=new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,item);
        category_ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(category_ad);

        if(ID!=-1&&check_id==1)
        {

            if(fix_income_outcome.equals("收入"))
            {
                fix_type = it_fix.getStringExtra("類別");
                if(fix_type.equals("薪水"))
                {
                    category.setSelection(1);
                }
                if(fix_type.equals("理財"))
                {
                    category.setSelection(2);
                }
                if(fix_type.equals("兼職"))
                {
                    category.setSelection(3);
                }
                if(fix_type.equals("禮金"))
                {
                    category.setSelection(4);
                }
                if(fix_type.equals("其他"))
                {
                    category.setSelection(5);
                }
                check_id=0;
            }
            else
            {
                fix_type = it_fix.getStringExtra("類別");
                if(fix_type.equals("購物"))
                {
                    category.setSelection(1);
                }
                if(fix_type.equals("食物"))
                {
                    category.setSelection(2);
                }
                if(fix_type.equals("娛樂"))
                {
                    category.setSelection(3);
                }
                if(fix_type.equals("日常用品"))
                {
                    category.setSelection(4);
                }
                if(fix_type.equals("交通"))
                {
                    category.setSelection(5);
                }
                if(fix_type.equals("醫療"))
                {
                    category.setSelection(6);
                }
                if(fix_type.equals("其他"))
                {
                    category.setSelection(7);
                }
            }
            check_id=0;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        db.close();
    }
}