package com.example.koda.loonyliterature;

import android.app.Application;

public class MyApplication extends Application {

    private static String madLib;
    private static MadLib madLibObject;
    private static MadLib completedMadLibObject;
    private static String completedString;

    public static void setMadLib(String lib){
        madLib = lib;
    }

    public static String getMadLib(){
        return madLib;
    }

    public static void setMadLibObject(MadLib lib){
        madLibObject = lib;
    }

    public static MadLib getMadLibObject(){
        return madLibObject;
    }

    public static void setCompletedMadLibObject(MadLib lib){
        completedMadLibObject = lib;
    }

    public static MadLib getCompletedMadLibObject(){
        return completedMadLibObject;
    }

    public static void setMadLibString(String madLib){ completedString = madLib;}

    public static String getMadlibString(){return completedString;}
}
