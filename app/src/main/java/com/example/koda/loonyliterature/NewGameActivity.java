package com.example.koda.loonyliterature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NewGameActivity extends AppCompatActivity
        implements View.OnClickListener {

    private MadLib madlib;
    private Button finishedButton;
    private ArrayList<EditText> inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        madlib = MyApplication.getMadLibObject();
        if(madlib != null){
            inputs = new ArrayList<EditText>();
            setContentView(R.layout.activity_start_story);
            setMadLibInput();
            setButton();
        }
    }

    private void setMadLibInput() {
        TextView text = findViewById( R.id.textView2);
        LinearLayout linearLayout = findViewById(R.id.InputSection);
        text.setText(madlib.title);
        for (String input :
                madlib.blanks) {
            TextView newText = new TextView(getApplicationContext());
            newText.setText(input);
            linearLayout.addView(newText);
            EditText newInput = new EditText((getApplicationContext()));
            inputs.add(newInput);
            linearLayout.addView(newInput);
        }
    }

    private void setButton() {
        finishedButton = findViewById(R.id.FinishedButton);
        finishedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        for(int i = 0; i < madlib.blanks.size(); i++){
            madlib.blanks.set(i, inputs.get(i).getText().toString());
            MyApplication.setCompletedMadLibObject(madlib);
        }
        Intent intent = new Intent(getApplicationContext(), StartGameActivity.class);
        startActivity(intent);
    }
}
