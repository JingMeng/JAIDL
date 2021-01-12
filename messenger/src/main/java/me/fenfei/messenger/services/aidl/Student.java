package me.fenfei.messenger.services.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 编译器就能保证文件的存在
 * 不需要检查，类的位置，是否存在
 * 1.是否实现了Parcelable，是否有CREATOR 常量
 * 2.是否实现了Parcelable.Creator
 * <p>
 * 以上的所有检查只为了找到我们所需要的类
 * <p>
 * 因为是使用反射，我们
 * Class<?> parcelableClass = Class.forName(name, false , *parcelableClassLoader); 还需要解释一下这句话
 * <p>
 * https://blog.csdn.net/fengyuzhengfan/article/details/38086743
 * 按照这个加载方式就会出错
 * 还是还原的搞一个级别的问题
 */
public class Student implements Parcelable {

    public String name;


    public Student(String name) {
        this.name = name;
    }

    void test() {
        try {
            Class.forName(Student.class.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
