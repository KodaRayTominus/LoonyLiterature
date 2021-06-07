package com.example.koda.loonyliterature;

import android.app.Activity;
import android.os.Bundle;

public class SavedStoriesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_stories);
    }
}


//                        try{
//                            InputStream tempInput = openFileInput("savedMadLibs");
//                            //print out saved stories
//
//                        }
//                        catch (IOException e){
//                            e.printStackTrace();
//                        }