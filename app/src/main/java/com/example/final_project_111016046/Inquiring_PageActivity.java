package com.example.final_project_111016046;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Inquiring_PageActivity extends AppCompatActivity
        implements View.OnClickListener,AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener,AdapterView.OnItemLongClickListener{

    static final String db_name="money_manager";
    static final String tb_name="money_managers";

    static final String[] FROM = new String[]{"income_outcome","topic","type","price","date"};
    String[] category1={"","薪水","理財","兼職","禮金","其他"};
    String[] category2={"","購物","食物","娛樂","日常用品","交通","醫療","其他"};
    String[] category3={""};
    Spinner income_outcome_spinner,type_spinner;

    SQLiteDatabase db;
    Cursor cur;
    SimpleCursorAdapter adapter;
    ListView lv_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiring_page);

        income_outcome_spinner=(Spinner)findViewById(R.id.income_outcome_spinner);
        type_spinner=(Spinner)findViewById(R.id.type_spinner);

        income_outcome_spinner.setOnItemSelectedListener(this);
        type_spinner.setOnItemSelectedListener(this);

        db=openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        String createTable="CREATE TABLE IF NOT EXISTS "+tb_name+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,date varchar(32),income_outcome varchar(32),topic varchar(32),type varchar(32),price varchar(32))";
        db.execSQL(createTable);

    }
    public void requery()
    {
        String income_outcomeStr=(String) income_outcome_spinner.getSelectedItem();
        String typeStr=(String)type_spinner.getSelectedItem();

        if(!typeStr.equals(""))
        {
            cur=db.rawQuery("SELECT * FROM "+tb_name+" WHERE income_outcome = '" + income_outcomeStr + "' AND type = '" + typeStr + "'" ,null);
            if(cur.getCount()==0)
            {
                Toast tos=Toast.makeText(this,"目前尚無資料", Toast.LENGTH_SHORT);
                tos.show();
            }
            adapter=new SimpleCursorAdapter(
                    this,
                    R.layout.item_search,
                    cur,
                    FROM,
                    new int[]{R.id.a_income_outcome_search,R.id.a_topic_search,R.id.a_type_search,R.id.a_amount_search,R.id.date_search},
                    0
            );
            lv_search=(ListView)findViewById(R.id.lv_search);
            lv_search.setAdapter(adapter);
            lv_search.setOnItemLongClickListener((AdapterView.OnItemLongClickListener) this);
            lv_search.setItemsCanFocus(true);
        }
        else
        {
            Toast tos=Toast.makeText(this,"請重新選擇條件", Toast.LENGTH_SHORT);
            tos.show();
        }
    }
    public void search_onclick(View v)
    {
        requery();
    }
    public void go_back(View v)
    {
        Intent it = new Intent(this, Main_PageActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.type_spinner)
        {
            Typeface tf = getResources().getFont(R.font.baboo);
            TextView child = (TextView) parent.getChildAt(0);
            child.setTextSize(24);
            child.setTypeface(tf);
            child.setTextColor(Color.WHITE);
            return;
        }
        String[] item={};
        if(position==0)
        {
            item=category3;
        }
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
        type_spinner.setAdapter(category_ad);

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
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        cur.moveToPosition(position);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Inquiring_PageActivity.this);
        alertDialog.setTitle("修改或刪除");
        alertDialog.setPositiveButton("修改",(dialog, which) -> {
                Intent it = new Intent(this,Add_PageActivity.class);
                it.putExtra("id",cur.getInt(0));
                it.putExtra("日期",cur.getString(1));
                it.putExtra("收支出",cur.getString(2));
                it.putExtra("主題",cur.getString(3));
                it.putExtra("類別",cur.getString(4));
                it.putExtra("金額",cur.getString(5));
                startActivityForResult(it,100);
        });
        alertDialog.setNegativeButton("刪除",(dialog, which) -> {
            db.delete(tb_name, "_id="+cur.getInt(0),null);
                requery();
        });
        alertDialog.setNeutralButton("取消",(dialog, which) -> {
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
        return true;
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        db.close();
    }
}