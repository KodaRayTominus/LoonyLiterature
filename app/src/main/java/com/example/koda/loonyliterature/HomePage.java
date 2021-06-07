package com.example.koda.loonyliterature;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class HomePage extends AppCompatActivity
    implements View.OnClickListener {

    private Button newStoryButton;
    private Button savedStoriesButton;
    private Button categoriesButton;
    private Button settingsButton;
    private Button exitButton;
    private MadLib currentMadLib;
    private SharedPreferences prefs;

    private Context mContext;
    private Activity mActivity;
    private Gson gson = new Gson();

    private CoordinatorLayout mCLayout;
    private String mUrlString = "http://madlibz.herokuapp.com/api/random?minlength=5&maxlength=25";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mContext = getApplicationContext();
        mActivity = HomePage.this;
        getMenuInflater().inflate(R.menu.activity_menu, menu);
        getMadLibString();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.menu_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        // save the instance variables
        SharedPreferences.Editor editor = prefs.edit();
        editor.apply();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        GetMainButtons();


        // set the default values for the preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    private void GetMainButtons(){
        newStoryButton = findViewById(R.id.NewGameButton);
        newStoryButton.setOnClickListener(this);
        savedStoriesButton = findViewById(R.id.SavedStoriesButton);
        savedStoriesButton.setOnClickListener(this);
        categoriesButton = findViewById(R.id.CategoriesButton);
        categoriesButton.setOnClickListener(this);
        settingsButton = findViewById(R.id.SettingsButton);
        settingsButton.setOnClickListener(this);
        exitButton = findViewById(R.id.ExitButton);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button b = getGameButton(v.getId());
        switch (b.getId()){
            case R.id.NewGameButton:
                Intent intent = new Intent(getApplicationContext(), NewGameActivity.class);
                startActivity(intent);
                break;
            case R.id.SavedStoriesButton:
                startActivity(new Intent(getApplicationContext(), SavedStoriesActivity.class));
                break;
            case R.id.CategoriesButton:
                startActivity(new Intent(getApplicationContext(), CategoriesActivity.class));
                break;
            case R.id.SettingsButton:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.ExitButton:
                break;
        }

    }

    private Button getGameButton(int newGameButton) {
        return findViewById(newGameButton);
    }

    private void getMadLibString() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                mUrlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        saveLibAsMadLib(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when get error
                    }
                }
        );

        MySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }




    public void saveLibAsMadLib(String lib) {
        Gson g = new Gson();
        try {
            JSONObject j = new JSONObject(lib);
            lib = j.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        currentMadLib = g.fromJson(lib, MadLib.class);
        MyApplication.setMadLibObject(currentMadLib);
    }
}

