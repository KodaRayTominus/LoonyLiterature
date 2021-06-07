package com.example.koda.loonyliterature;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MadLib implements Parcelable {

    ArrayList<String> blanks = new ArrayList<>();

    String title = "";

    ArrayList<String> value = new ArrayList<>();




    MadLib(Parcel in){
         in.readStringList(blanks);
         title = in.readString();
         in.readStringList(value);

    }

    MadLib(ArrayList<String> blanks, ArrayList<String> values, String title){
        this.blanks = blanks;
        title = title;
        this.value = values;
    }

    MadLib(ArrayList<String> blanks, ArrayList<String> values){
        new MadLib(blanks, values, "No Title");
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    // Write data in any order

        dest.writeStringList(blanks);
        dest.writeStringList(value);
        dest.writeString(title);
    }

    public static final Parcelable.Creator<MadLib> CREATOR = new Parcelable.Creator<MadLib>() {
        public MadLib createFromParcel(Parcel in) {
            return new MadLib(in);
        }

        public MadLib[] newArray(int size) {
            return new MadLib[size];
        }
    };


}
