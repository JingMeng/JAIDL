package me.fenfei.messenger.services.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {

    public String name;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    protected Student(Parcel in) {
        this.name = in.readString();
    }
//    Interface that must be implemented and provided as a public CREATOR field that generates instances of your Parcelable class from a Parcel.
    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
