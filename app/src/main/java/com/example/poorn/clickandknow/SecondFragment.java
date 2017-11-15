package com.example.poorn.clickandknow;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by poorn on 11/7/2017.
 */

public class SecondFragment extends Fragment implements UpdateableFragment {

    public static SecondFragment instance = null;
    TextView mTitle;
    TextView mBody;
    ImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second, container, false);
        TextView textView = (TextView) v.findViewById(R.id.tvFragSecond);
        textView.setText(getArguments().getString("msg"));
        mTitle = (TextView) v.findViewById(R.id.title);
        mBody = (TextView) v.findViewById(R.id.body);
        imageView = (ImageView) v.findViewById(R.id.image);
        return v;
    }

    public static SecondFragment newInstance(String text){
        if(instance == null){
            instance = new SecondFragment();
            // sets data to bundle
            Bundle bundle = new Bundle();
            bundle.putString("msg", text);
            // set data to fragment
            instance.setArguments(bundle);
            return instance;
        } else {
            return instance;
        }
    }

    @Override
    public void update(UpdateData data){
        Log.d("Update2","In update of secondfrag");
        // this method will be called for every fragment in viewpager
        // so check if update is for this fragment
        if(data.position == 1){
            showToast("It is" + data.caption);
            // do whatever you want to update your UI
            mTitle.setText(data.caption);
            mBody.setText(data.tags.get(0));
            imageView.setImageBitmap(data.ObjectImage);

        }
    }

    private void showToast(final String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}