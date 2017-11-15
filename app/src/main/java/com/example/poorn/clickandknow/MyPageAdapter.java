package com.example.poorn.clickandknow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by poorn on 11/9/2017.
 */

public class MyPageAdapter extends FragmentPagerAdapter {

    public MyPageAdapter(FragmentManager fm) {
        super(fm);
    }
    public UpdateData updateData;

    public void update(UpdateData xyzData) {
        Log.d("Updatemethod","in update of MyPageAdapter");
        this.updateData = xyzData;
        notifyDataSetChanged();
    }
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return FirstFragment.newInstance("FirstFragment, Instance 1");
            case 1:
                return SecondFragment.newInstance("SecondFragment, Instance 1");
            default:
                return FirstFragment.newInstance("FirstFragment, Default");
        }
    }

    @Override
    public int getCount() {
        return Config.NUMBER_OF_PAGES;
    }

}
