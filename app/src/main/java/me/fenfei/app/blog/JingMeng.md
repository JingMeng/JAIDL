
###基本概念与问题的提出：

1. 进程是程序的一个运行实例，以区别于"程序这一静态概念；而线程则是CPU调度的基本的单位。

2. 如果两个对象处于同一个进程空间中，那么内存区域应该是可以共享的(操作系统基本知识)<br/>
   也就是不同进程间内存区域是不共享的，并不是不同的"程序"
  
3. 同时在官网，我们可以找到下面这句话：
 > By default, all components of the same application run in the same process and most applications should not change this. However, if you find that you need to control which process a certain component belongs to, you can do so in the manifest file.
 [官网介绍地址](https://stuff.mit.edu/afs/sipb/project/android/docs/guide/components/processes-and-threads.html)
  
 上面的文档告诉我们，我们所创建的四大组件默认都在主线程中
 
 Tips：我们可以通过 android:process 来给Activity配置一个单独的进程。[官网介绍地址](https://developer.android.google.cn/guide/topics/manifest/activity-element?hl=zh-cn#proc)
  
4.基本验证

  创建三个Activity，基本代码如下：

	public class MainActivity extends AppCompatActivity {
	
	    public static final String TAG = "print_value";
	    public static int value = -1;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	
	        setTitle("MainActivity");
	
	        findViewById(R.id.to_next_activity).setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                value = 1;
	                Log.i(TAG, value + "=====to==Main2Activity===========");
	                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
	                startActivity(intent);
	            }
	        });
	        findViewById(R.id.to_next_activity2).setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                value = 2;
	                Log.i(TAG, value + "====to=====Main3Activity=========");
	                Intent intent = new Intent(MainActivity.this, Main3Activity.class);
	                startActivity(intent);
	            }
	        });
	    }
	}
	
	public class Main2Activity extends AppCompatActivity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main2);
	
	        setTitle("Main2Activity");
	
	
	        Log.i(TAG, value + "=====this==Main2Activity===========");
	    }
	}
	public class Main3Activity extends AppCompatActivity {
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main3);
	
	        setTitle("Main3Activity");
	        Log.i(TAG, value + "=====this==Main3Activity===========");
	
	
	    }
	}

清单配置

	  <activity
	      android:name=".Main3Activity"
	      android:process=":remote" />
	  <activity android:name=".Main2Activity" />
	  <activity android:name=".MainActivity">
	      <intent-filter>
	          <action android:name="android.intent.action.MAIN" />
	
	          <category android:name="android.intent.category.LAUNCHER" />
	      </intent-filter>
	  </activity>



 注意Main3Activity的清单配置

 我们将得到如下打印：

  1=====to==Main2Activity===========</br>
  1=====this==Main2Activity===========</br>
  2====to=====Main3Activity=========</br>
  -1=====this==Main3Activity===========（得到如下打印，记得切换Logcat的展示进程）</br>

 结论： 这个打印足以证明多进程之前内存区域不共享 


常见的跨进程方式有：

AIDL ，Messager，Broadcast，ContentProvider ，甚至还可以使用文件和Scoket

针对几种方式，《Android艺术开发探索》提供了如下对比图

![几种方式比较](pic/几种方式比较.png)

 
### AIDL的介绍
 
1. 基本定义：AIDL是Android Interface Definition Languagee的缩写。从名称看它是一种语言，而且是专门用于描述接口的语言。准确的来说，他是用于定义客户端、度无端通信接口的一种描述语言。
   
   // Add.aidl
   package me.fenfei.app.aidl;
   
   // Declare any non-default types here with import statements
   
   interface Add {
       /**
        * Demonstrates some basic types that you can use as parameters
        * and return values in AIDL.
        */
       void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
               double aDouble, String aString);
   }


// Add.aidl
package me.fenfei.app.aidl;

// Declare any non-default types here with import statements

interface Add {

   int add(int a,int b);

}



Rebulid Project
  
  
/build/generated/aidl_source_output_dir/debug/compileDebugAidl/out

/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package me.fenfei.app.aidl;
// Declare any non-default types here with import statements

public interface Add extends android.os.IInterface
{
  /** Default implementation for Add. */
  public static class Default implements me.fenfei.app.aidl.Add
  {
    @Override public int add(int a, int b) throws android.os.RemoteException
    {
      return 0;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements me.fenfei.app.aidl.Add
  {
    private static final java.lang.String DESCRIPTOR = "me.fenfei.app.aidl.Add";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an me.fenfei.app.aidl.Add interface,
     * generating a proxy if needed.
     */
    public static me.fenfei.app.aidl.Add asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof me.fenfei.app.aidl.Add))) {
        return ((me.fenfei.app.aidl.Add)iin);
      }
      return new me.fenfei.app.aidl.Add.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_add:
        {
          data.enforceInterface(descriptor);
          int _arg0;
          _arg0 = data.readInt();
          int _arg1;
          _arg1 = data.readInt();
          int _result = this.add(_arg0, _arg1);
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements me.fenfei.app.aidl.Add
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public int add(int a, int b) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(a);
          _data.writeInt(b);
          boolean _status = mRemote.transact(Stub.TRANSACTION_add, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().add(a, b);
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      public static me.fenfei.app.aidl.Add sDefaultImpl;
    }
    static final int TRANSACTION_add = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    public static boolean setDefaultImpl(me.fenfei.app.aidl.Add impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static me.fenfei.app.aidl.Add getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public int add(int a, int b) throws android.os.RemoteException;
}
