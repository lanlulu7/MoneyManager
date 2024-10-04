package com.example.final_project_111016046;

import static android.graphics.Color.argb;
import static android.graphics.Color.rgb;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

public class Analysing_PageActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener{
    SQLiteDatabase db;
    Cursor cur;
    static final String db_name="money_manager";
    static final String tb_name="money_managers";

    static final String[] FROM = new String[]{"income_outcome","topic","type","price"};
    String income = "收入";
    String outcome = "支出";
    float income_sum=0;
    float outcome_sum=0;

    String getPartIncome;
    String getPartOutcome;

    float salary=0;
    float management=0;
    float part_time=0;
    float bonus=0;
    float other=0;

    float shopping=0;
    float food=0;
    float entertainment=0;
    float daily=0;
    float transportation=0;
    float medical=0;
    float others=0;
    Spinner spinner_click;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysing_page);

        spinner_click = (Spinner)findViewById(R.id.spinner_click);
        spinner_click.setOnItemSelectedListener(this);

        db=openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        String createTable="CREATE TABLE IF NOT EXISTS "+tb_name+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,date varchar(32),income_outcome varchar(32),topic varchar(32),type varchar(32),price varchar(32))";
        db.execSQL(createTable);

        Cursor cur1=db.rawQuery("SELECT * FROM "+tb_name, null);
        if(cur1.moveToFirst())
        {
            do {
                if(cur1.getString(2).equals(income))
                {
                    if(cur1.getString(4).equals("薪水"))
                    {
                        getPartIncome = cur1.getString(5);
                        salary = salary+Float.parseFloat(getPartIncome);
                    }
                    if(cur1.getString(4).equals("理財"))
                    {
                        getPartIncome = cur1.getString(5);
                        management = management+Float.parseFloat(getPartIncome);
                    }
                    if(cur1.getString(4).equals("兼職"))
                    {
                        getPartIncome = cur1.getString(5);
                        part_time = part_time+Float.parseFloat(getPartIncome);
                    }
                    if(cur1.getString(4).equals("禮金"))
                    {
                        getPartIncome = cur1.getString(5);
                        bonus = bonus+Float.parseFloat(getPartIncome);
                    }
                    if(cur1.getString(4).equals("其他"))
                    {
                        getPartIncome = cur1.getString(5);
                        other = other+Float.parseFloat(getPartIncome);
                    }
                }
                else
                {
                    if(cur1.getString(4).equals("購物"))
                    {
                        getPartOutcome = cur1.getString(5);
                        shopping = shopping+Float.parseFloat(getPartOutcome);
                    }
                    if(cur1.getString(4).equals("食物"))
                    {
                        getPartOutcome = cur1.getString(5);
                        food = food+Float.parseFloat(getPartOutcome);
                    }
                    if(cur1.getString(4).equals("娛樂"))
                    {
                        getPartOutcome = cur1.getString(5);
                        entertainment = entertainment+Float.parseFloat(getPartOutcome);
                    }
                    if(cur1.getString(4).equals("日常用品"))
                    {
                        getPartOutcome = cur1.getString(5);
                        daily = daily+Float.parseFloat(getPartOutcome);
                    }
                    if(cur1.getString(4).equals("交通"))
                    {
                        getPartOutcome = cur1.getString(5);
                        transportation = transportation+Float.parseFloat(getPartOutcome);
                    }
                    if(cur1.getString(4).equals("醫療"))
                    {
                        getPartOutcome = cur1.getString(5);
                        medical = medical+Float.parseFloat(getPartOutcome);
                    }
                    if(cur1.getString(4).equals("其他"))
                    {
                        getPartOutcome = cur1.getString(5);
                        others = others+Float.parseFloat(getPartOutcome);
                    }
                }

            }while(cur1.moveToNext());
            income_sum = salary+management+part_time+bonus+other;
            outcome_sum =  shopping+food+entertainment+daily+transportation+medical+others;
        }
    }
    public void go_back(View v)
    {
        Intent it = new Intent(this, Main_PageActivity.class);
        startActivity(it);
        finish();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        TextView no_image = (TextView)findViewById(R.id.no_image);
        PieChart income_chart = (PieChart) findViewById(R.id.income_chart);
        if(position==0)
        {
            if(income_sum!=0)
            {
                no_image.setText("");
                income_chart.setAlpha(1);
                salary=salary/income_sum*100;
                management=management/income_sum*100;
                part_time=part_time/income_sum*100;
                bonus=bonus/income_sum*100;
                other=other/income_sum*100;




                income_chart.setUsePercentValues(true);//使用百分比
                income_chart.getDescription().setEnabled(false);//圖表的描述
                income_chart.setDrawEntryLabels(false);//圖表只顯示餅圖上百分比不顯示文字
                income_chart.setRotationEnabled(false);//圖表是否可以手動旋轉
                income_chart.setMinAngleForSlices(10f);


                Legend legend = income_chart.getLegend();//是否顯示圖例
                legend.setTextSize(20);
                Typeface tf = getResources().getFont(R.font.baboo);
                legend.setTypeface(tf);
                legend.setEnabled(true);
                legend.setWordWrapEnabled(true);

                income_chart.setDrawHoleEnabled(true);// 內圓
                income_chart.setHoleRadius(50f);// 设置PieChart内部圆的半徑
                income_chart.setHoleColor(argb(0, 0, 0, 0));
                income_chart.setEntryLabelTypeface(tf);

                List<PieEntry> strings = new ArrayList<>(); //
                if(salary>0)
                {
                    strings.add(new PieEntry(salary,"薪水"));
                }
                if(management>0)
                {
                    strings.add(new PieEntry(management,"理財"));
                }
                if(part_time>0)
                {
                    strings.add(new PieEntry(part_time,"兼職"));
                }
                if(bonus>0)
                {
                    strings.add(new PieEntry(bonus,"禮金"));
                }
                if(other>0)
                {
                    strings.add(new PieEntry(other,"其他"));
                }







                PieDataSet dataSet = new PieDataSet(strings,"收入(%)");

                ArrayList<Integer> colors = new ArrayList<Integer>();
                colors.add(rgb(255,102,102));//紅色
                colors.add(rgb(255,141,0));//橙色
                colors.add(rgb(255,215,0));//黃色
                colors.add(rgb(12,196,172));//綠色
                colors.add(rgb(78,114,184));//藍色

                dataSet.setColors(colors);

                PieData pieData = new PieData(dataSet);
                pieData.setValueFormatter(new PercentFormatter());
                pieData.setValueTextSize(15f);
                pieData.setValueTextColor(Color.WHITE);
                income_chart.setData(pieData);
                income_chart.highlightValue(null);
                income_chart.invalidate(); //更新畫面
            }
            else
            {
                    no_image.setText("目前尚無資料");
                    income_chart.setAlpha(0);

            }
        }
        else
        {
            if(outcome_sum!=0)
            {
                no_image.setText("");
                income_chart.setAlpha(1);
                shopping=shopping/outcome_sum*100;
                food=food/outcome_sum*100;
                entertainment=entertainment/outcome_sum*100;
                daily=daily/outcome_sum*100;
                transportation=transportation/outcome_sum*100;
                medical=medical/outcome_sum*100;
                others=others/outcome_sum*100;

                income_chart.setUsePercentValues(true);//使用百分比
                income_chart.getDescription().setEnabled(false);//圖表的描述
                income_chart.setDrawEntryLabels(false);//圖表只顯示餅圖上百分比不顯示文字
                income_chart.setRotationEnabled(false);//圖表是否可以手動旋轉
                income_chart.setMinAngleForSlices(10f);

                Legend legend = income_chart.getLegend();//是否顯示圖例
                legend.setTextSize(20);
                Typeface tf = getResources().getFont(R.font.baboo);
                legend.setTypeface(tf);
                legend.setEnabled(true);
                legend.setWordWrapEnabled(true);

                income_chart.setDrawHoleEnabled(true);// 內圓
                income_chart.setHoleRadius(50f);// 设置PieChart内部圆的半徑
                income_chart.setHoleColor(argb(0, 0, 0, 0));
                income_chart.setEntryLabelTypeface(tf);

                List<PieEntry> strings = new ArrayList<>(); //
                if(shopping>0)
                {
                    strings.add(new PieEntry(shopping,"購物"));
                }
                if(food>0)
                {
                    strings.add(new PieEntry(food,"食物"));
                }
                if(entertainment>0)
                {
                    strings.add(new PieEntry(entertainment,"娛樂"));
                }
                if(daily>0)
                {
                    strings.add(new PieEntry(daily,"日常用品"));
                }
                if(transportation>0)
                {
                    strings.add(new PieEntry(transportation,"交通"));
                }
                if(medical>0)
                {
                    strings.add(new PieEntry(medical,"醫療"));
                }
                if(others>0)
                {
                    strings.add(new PieEntry(others,"其他"));
                }








                PieDataSet dataSet = new PieDataSet(strings,"支出(%)");
                ArrayList<Integer> colors = new ArrayList<Integer>();
                colors.add(rgb(255,102,102));//紅色
                colors.add(rgb(255,141,0));//橙色
                colors.add(rgb(255,215,0));//黃色
                colors.add(rgb(12,196,172));//綠色
                colors.add(rgb(78,114,184));//藍色
                colors.add(rgb(174,151,253));//紫色
                colors.add(rgb(119,136,153));//灰色

                dataSet.setColors(colors);

                PieData pieData = new PieData(dataSet);
                pieData.setValueFormatter(new PercentFormatter());
                pieData.setValueTextSize(15f);
                pieData.setValueTextColor(Color.WHITE);
                income_chart.setData(pieData);
                income_chart.highlightValue(null);
                income_chart.invalidate(); //更新畫面

            }
            else
            {
                no_image.setText("目前尚無資料");
                income_chart.setAlpha(0);
            }
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
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        db.close();
    }
}