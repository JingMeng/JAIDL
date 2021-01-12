package me.fenfei.messenger.services.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Student2 implements Parcelable {

    public String name;
    public ArrayList<String> mStrings;


    public Student2() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeStringList(this.mStrings);
    }

    protected Student2(Parcel in) {
        this.name = in.readString();
        this.mStrings = in.createStringArrayList();
//        this.mStrings = in.readArrayList(String.class.getClassLoader());
    }

    public static final Creator<Student2> CREATOR = new Creator<Student2>() {
        @Override
        public Student2 createFromParcel(Parcel source) {
            return new Student2(source);
        }

        @Override
        public Student2[] newArray(int size) {
            return new Student2[size];
        }
    };
}
