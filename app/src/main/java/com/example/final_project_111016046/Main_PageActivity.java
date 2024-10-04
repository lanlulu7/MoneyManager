package com.example.final_project_111016046;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;


public class Main_PageActivity extends AppCompatActivity {

    static final String db_name="money_manager";
    static final String tb_name="money_managers";

    static final String[] FROM = new String[]{"income_outcome","topic","type","price"};

    SQLiteDatabase db;
    int sum;
    int old_money=0;
    int new_money=0;
    String income="收入";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        ImageView money_state_img = (ImageView)findViewById(R.id.money_state_img);
        ImageView get_money_img = (ImageView)findViewById(R.id.get_money_img);
        ImageView loss_money_img = (ImageView)findViewById(R.id.loss_money_img);

        db=openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);
        String createTable="CREATE TABLE IF NOT EXISTS "+tb_name+"(_id INTEGER PRIMARY KEY AUTOINCREMENT,date varchar(32),income_outcome varchar(32),topic varchar(32),type varchar(32),price varchar(32))";
        db.execSQL(createTable);

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

        TextView sum_money = (TextView)findViewById(R.id.sum_money);
        sum_money.setText("總結餘:"+String.valueOf(sum));

        SharedPreferences sharedPref1 = getPreferences(Context.MODE_PRIVATE);

        old_money = sharedPref1.getInt("old_money",0);

        new_money=sum;
        if(new_money>0&&new_money<100)
        {
            money_state_img.setImageResource(R.drawable.a_little_money);
        }
        if(new_money>=100&&new_money<1000)
        {
            money_state_img.setImageDrawable(getResources().getDrawable( R.drawable.dollar));
        }
        if(new_money>=1000&&new_money<100000)
        {
            money_state_img.setImageDrawable(getResources().getDrawable( R.drawable.paper_money));
        }
        if(new_money>=100000&&new_money<1000000)
        {
            money_state_img.setImageDrawable(getResources().getDrawable( R.drawable.treasure_chest));
        }
        if(new_money>=1000000)
        {
            money_state_img.setImageDrawable(getResources().getDrawable( R.drawable.treasure));
        }
        if(new_money==0)
        {
            money_state_img.setImageResource(R.drawable.nomoney);
        }
        if(new_money<0)
        {
            money_state_img.setImageResource(R.drawable.bankruptcy);
        }

        new_money=new_money-old_money;
        if(new_money>0)
        {
            get_money_img.setImageResource(R.drawable.a_little_money);
            get_money_img.setAlpha(1.0f);
            AnimatorSet animatorSet = new AnimatorSet();
            ValueAnimator animatorX = ValueAnimator.ofFloat(0,100);
            animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    float value = (float)animation.getAnimatedValue();
                    get_money_img.setTranslationX((float) (0 - 4.55*value));
                }
            });
            ValueAnimator animatorY = ValueAnimator.ofFloat(0,100);
            animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    float value = (float)animation.getAnimatedValue();
                    get_money_img.setTranslationY((float) (0+ 5.5*value));
                }
            });
            ValueAnimator animatorZ = ValueAnimator.ofFloat(1, 0);
            animatorZ.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    float value = (float)animation.getAnimatedValue();
                    get_money_img.setAlpha(value);
                }
            });
            animatorZ.setDuration(100);

            animatorSet.playTogether(animatorX,animatorY);
            animatorSet.playSequentially(animatorX, animatorZ);

            animatorSet.setDuration(1000+100);
            animatorSet.start();

        }
        if(new_money<0)
        {
            loss_money_img.setImageResource(R.drawable.money_fiying);
            loss_money_img.setAlpha(1.0f);
            AnimatorSet animatorSet = new AnimatorSet();
            ValueAnimator animatorX = ValueAnimator.ofFloat(0,100);
            animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    float value = (float)animation.getAnimatedValue();
                    loss_money_img.setTranslationX(0 + 4*value);
                }
            });
            ValueAnimator animatorY = ValueAnimator.ofFloat(0,100);
            animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    float value = (float)animation.getAnimatedValue();
                    loss_money_img.setTranslationY((0- 5*value));
                }
            });
            ValueAnimator animatorZ = ValueAnimator.ofFloat(1, 0);
            animatorZ.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                    float value = (float)animation.getAnimatedValue();
                    loss_money_img.setAlpha(value);
                }
            });
            animatorZ.setDuration(100);

            animatorSet.playTogether(animatorX,animatorY);
            animatorSet.playSequentially(animatorX, animatorZ);

            animatorSet.setDuration(1000+100);
            animatorSet.start();
        }
        old_money=sum;
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("old_money", old_money);
        editor.apply();

    }


    public void gotoAdd_Page(View v) {
        Intent it = new Intent(this, Add_PageActivity.class);
        startActivity(it);
    }

    public void gotoView_Page(View v) {
        Intent it = new Intent(this, View_PageActicity.class);
        startActivity(it);
    }

    public void gotoInquiring_Page(View v) {
        Intent it = new Intent(this, Inquiring_PageActivity.class);
        startActivity(it);
    }

    public void gotoAnalysing_Page(View v) {
        Intent it = new Intent(this, Analysing_PageActivity.class);
        startActivity(it);
    }
    public void gotoMain(View v) {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
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
    protected void onDestroy()
    {
        super.onDestroy();
        db.close();
    }

}