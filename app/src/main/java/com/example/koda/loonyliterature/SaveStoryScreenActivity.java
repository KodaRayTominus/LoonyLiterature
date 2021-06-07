package com.example.koda.loonyliterature;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SaveStoryScreenActivity extends AppCompatActivity
        implements View.OnClickListener{


    private Button CancelButton;
    private Button SaveButton;

    private EditText Category;
    private EditText Name;

    private File fileDirectory;
    private File savedMadLibs;
    private String fileContents;
    private OutputStream outputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_save_screen);

        setButtons();

    }

    private void setButtons(){
        CancelButton = getGameButton(R.id.CancelButton);
        CancelButton.setOnClickListener(this);
        SaveButton = getGameButton(R.id.SaveButton);
        SaveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button b = getGameButton(v.getId());
        switch (b.getId()){
            case R.id.CancelButton:
                finish();
                break;
            case R.id.SaveButton:
                //save to file
                saveToFile();
        }

    }

    private void saveToFile() {
        fileDirectory = getApplicationContext().getFilesDir();
        String[] temp = getApplicationContext().fileList();
        for(int i = 0; i < temp.length; i++){
            if(temp[i].equals("savedMadLibs")){
                try{
                    InputStream tempInput = openFileInput("savedMadLibs");
                    fileContents = tempInput.toString();
                    fileContents += "/n" + MyApplication.getCompletedMadLibObject().title + "&&&" +  MyApplication.getMadlibString();

                }
                catch (IOException e){
                    e.printStackTrace();
                }
                break;
            }
            fileContents = MyApplication.getCompletedMadLibObject().title + "&&&" + MyApplication.getMadlibString();
        }
        try {
            outputStream = openFileOutput(Name.getText().toString(), getApplicationContext().MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Button getGameButton(int newGameButton) {
        return findViewById(newGameButton);
    }
}
