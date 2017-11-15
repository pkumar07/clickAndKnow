package com.example.poorn.clickandknow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import com.microsoft.projectoxford.vision.VisionServiceClient;


public class MainActivity extends FragmentActivity
    implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener{


    private RadioGroup radioGroup;
    ViewPager pager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(this);
        Log.d("stopkidding","stopkidding");
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);


    }
    @Override
    public void onPageScrolled(int position, float v, int i) {
    }

    @Override
    public void onPageSelected(int position) {
        switch(position) {
            case 0:
                radioGroup.check(R.id.radioButton1);
                break;
            case 1:
                radioGroup.check(R.id.radioButton2);
                break;
            default:
                radioGroup.check(R.id.radioButton1);
        }
    }

    @Override
    public void onPageScrollStateChanged(int position) {
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId) {
            case R.id.radioButton1:
                pager.setCurrentItem(0);
                break;
            case R.id.radioButton2:
                pager.setCurrentItem(1);
                break;
        }
    }









}
