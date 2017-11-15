package com.example.poorn.clickandknow;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.gson.Gson;

import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;
import com.microsoft.projectoxford.vision.contract.Tag;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by poorn on 11/7/2017.
 */

public class FirstFragment extends Fragment implements UpdateableFragment {

    private static FirstFragment instance = null;
    Button picture = null;
    Bitmap imageBitMap;
    private VisionServiceClient client;
    public static UpdateData updateData;
    AnalysisResult analysisResult;
    String result;
    MyPageAdapter myPageAdapter;
    private Camera mCamera;
    private CameraPreview mPreview;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public FirstFragment(){
    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        TextView textView = (TextView) v.findViewById(R.id.tvFragFirst);
        textView.setText(getArguments().getString("msg"));
        mCamera = getCameraInstance();
        Log.d("cam_instance","camera instance");
        mPreview = new CameraPreview(getContext(),mCamera);
        Log.d("cam_preview","camera preview");
        FrameLayout preview = (FrameLayout) v.findViewById(R.id.camera_preview);
        preview.addView(mPreview);


        picture = (Button) v.findViewById(R.id.picture);


        picture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //take pic logic
                //set imageBitMap to some value
                mCamera.takePicture(null, null, mPicture);

                if (client == null) {
                    client = new VisionServiceRestClient(Config.MICROSOFT_DEVELOPER_KEY_CV, Config.MICROSOFT_ENDPOINT);
                    Log.d("Onclick", "In on click");
                }

                try{
                    imageBitMap = BitmapFactory.decodeResource(getResources(),R.drawable.images);
                    if(imageBitMap != null){
                        Log.d("Called_recognize","Recognize me");
                        doRecognize();
                    }

                }
                catch(Exception e){}

            }
        });
        return v;
    }

    @Override
    public void update(UpdateData data) {
        // this method will be called for every fragment in viewpager
        // so check if update is for this fragment
        Log.d("In update0","Update 0");
        if (data.position == 0) {
            // do whatever you want to update your UI
            Log.d("In update0","Update 0, hello");
        }
    }

    public static FirstFragment newInstance(String text) {
        if (instance == null) {
            instance = new FirstFragment();
            Bundle bundle = new Bundle();
            bundle.putString("msg", text);
            instance.setArguments(bundle);
            return instance;
        } else {
            return instance;
        }

    }

    public Bitmap getBitmap(){
        return imageBitMap;
    }

    public void doRecognize() {
        Log.d("imageBitmap",imageBitMap.toString());
        try {
            Log.d("in do recognize","in do Recognize ");
            new doRequest().execute();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private String process() throws VisionServiceException, IOException {
        Gson gson = new Gson();
        Log.d("Inprocess", "Inprocess");
        String[] features = new String[]{"Tags", "Description"};
        String[] details = {};
        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        imageBitMap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());
        Log.d("Successfully reached", "Successfully reached here");
        analysisResult = this.client.analyzeImage(inputStream, features,details);
        result = gson.toJson(analysisResult);



        //Read out aloud the data in the stringBuilder

        Log.d("result123", result);
        return result;
    }

    private class doRequest extends AsyncTask<String, String, String> {
        // Store error message
        private Exception e = null;


        @Override
        protected String doInBackground(String... args) {
            try {
                Log.d("Start process","Starting the process");
                return process();
            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            // Display based on error existence

            if (e != null) {
                Log.d("Error: " , e.getMessage());
                this.e = null;
            } else {
                Gson gson = new Gson();

                analysisResult = new Gson().fromJson(result, AnalysisResult.class);
                StringBuilder stringBuilder = new StringBuilder();
                for(Caption caption: analysisResult.description.captions){
                    stringBuilder.append(caption.text);
                }
                Log.d("This is caption", stringBuilder.toString());
                ArrayList<String> sb = new ArrayList<>();
                //Contains a list of tags
                for(Tag t : analysisResult.tags){
                    Log.d("Adding tag", t.name.toString());
                    sb.add(t.name.toString());
                }

                updateData = new UpdateData();
                updateData.tags = sb;
                Log.d("These are tags", sb.toString());
                updateData.ObjectImage = getBitmap();
                Log.d("Updated object", "hey");
                updateData.caption = stringBuilder.toString();
                Log.d("Udated caption", updateData.caption);
                myPageAdapter = new MyPageAdapter(getFragmentManager());
                updateData.position = 1;
                Log.d("Calling update method", "calling update method");
                myPageAdapter.update(updateData);
                SecondFragment.instance.update(updateData);




            }


        }
    }





}