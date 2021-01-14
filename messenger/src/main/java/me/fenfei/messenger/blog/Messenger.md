#Messenger学习，理解


##1.基本知识

###1.1关于Parcelable
 
  如果仅仅是简单的传递8中基本类型，没有什么好说，但是我们要传递对象，就不得不说Parcelable，为此在学习Messenger之前我们需要再次了解一下[Parcelable](https://developer.android.google.cn/guide/components/aidl#Bundles)

  在使用`.aidl`文件的时候---`通过bundle传递`，我们需要按照下面的格式

	// IRectInsideBundle.aidl
	package com.example.android;
	
	/** Example service interface */
	interface IRectInsideBundle {
	    /** Rect parcelable is stored in the bundle with key "rect" */
	    void saveRect(in Bundle bundle);
	}

	
	private final IRectInsideBundle.Stub binder = new IRectInsideBundle.Stub() {
	    public void saveRect(Bundle bundle){
	        bundle.setClassLoader(getClass().getClassLoader());
	        Rect rect = bundle.getParcelable("rect");
	        process(rect); // Do more with the parcelable.
	    }
	};

 也就是需要特殊处理处理一下ClassLoader的问题，不然我们讲得到一个异常`ClassNotFoundException`
 
 在使用Messenger的时候我们同样也需要规避这个问题，我们将在后面进行讨论这是为什么。
 


###1.1[关于Messenger和AIDL的比较](https://developer.android.google.cn/guide/components/bound-services#Creating)

Messenger是执行进程间通信 (IPC) 最为简单的方式，因为 Messenger 会在单个线程中创建包含所有请求的队列，这样您就不必对服务进行线程安全设计。

为接口使用 Messenger 比使用 AIDL 更简单，因为 Messenger 会将所有服务调用加入队列。纯 AIDL 接口会同时向服务发送多个请求，那么服务就必须执行多线程处理。
对于大多数应用，服务无需执行多线程处理，因此使用 Messenger 可让服务一次处理一个调用。如果您的服务必须执行多线程处理，请使用 AIDL 来定义接口。

大多数应用不应使用 AIDL 来创建绑定服务，因为它可能需要多线程处理能力，并可能导致更为复杂的实现。


##2 [使用Messenger](https://developer.android.google.cn/guide/components/bound-services?#Messenger)

如需让接口跨不同进程工作，您可以使用 Messenger 为服务创建接口。采用这种方式时，服务会定义一个 Handler，用于响应不同类型的 Message 对象。此 Handler 是 Messenger 的基础，后者随后可与客户端分享一个 IBinder，以便客户端能利用 Message 对象向服务发送命令。
此外，客户端还可定义一个自有 Messenger，以便服务回传消息。

以下是对 Messenger 使用方式的摘要：

- 1.服务实现一个 Handler，由其接收来自客户端的每个调用的回调。
- 2.服务使用 Handler 来创建 Messenger 对象（该对象是对 Handler 的引用）。
- 3.Messenger 创建一个 IBinder，服务通过 onBind() 将其返回给客户端。
- 4.客户端使用 IBinder 将 Messenger（它引用服务的 Handler）实例化，然后再用其将 Message 对象发送给服务。
- 5.服务在其 Handler 中（具体而言，是在 handleMessage() 方法中）接收每个 Message。

这样，客户端便没有调用服务的方法。相反，客户端会传递服务在其 Handler 中接收的消息（Message 对象）。
 之前的案例是add方法，在add方法中，我们是直接互动的

下面这个简单的服务实例展示了如何使用 Messenger 接口：


###2.1. server

    static class IncomingHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
   
###2.2.server

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    Messenger mMessenger;

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        mMessenger = new Messenger(new IncomingHandler());
        return mMessenger.getBinder();
    }
    

###2.3. client

在需要的时候bindService
	
	bindService(intent, mConnection,Context.BIND_AUTO_CREATE);

onServiceConnected 的时候创建我们的消息传递对象

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = new Messenger(service);
            bound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
            bound = false;
        }
    };

记得及时回收资源

	 @Override
	 protected void onStop() {
	     super.onStop();
	     // Unbind from the service
	     if (bound) {
	         unbindService(mConnection);
	         bound = false;
	     }
	 }

	
###2.4.client

调用
 
    Message msg = Message.obtain(null, MessengerService.MSG_SAY_HELLO2, 0, 0);
 
    mService.send(msg);
 
传递对象参数
 
    Bundle bundle = new Bundle();
    bundle.putParcelable("msg", new Student("zyy"));
    msg.setData(bundle);

或者

    Bundle bundle = new Bundle();
    bundle.putParcelable("msg", new Student("zyy"));
    msg.obj = bundle;
  
但是不能：
  
    Bundle bundle = new Bundle();
    bundle.putParcelable("msg", new Student("zyy"));
    msg.obj = new Student("zyy");   
  
     
###2.5.如果想要双向消息传递：
  
需要在client添加
 
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

并且在发送消息的时候，把当前对象一起传递出去

	  Message msg = Message.obtain(null,MessengerService.MSG_REGISTER_CLIENT);
	  msg.replyTo = mMessenger;

server可以按照官方的交互进行模板代码书写

    /** Keeps track of all current registered clients. */
      ArrayList<Messenger> mClients = new ArrayList<Messenger>();
      
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SET_VALUE:
                //这个可以根据实际情况进行相应的回调回传消息
                    mValue = msg.arg1;
                    for (int i=mClients.size()-1; i>=0; i--) {
                        try {
                            mClients.get(i).send(Message.obtain(null, MSG_SET_VALUE, mValue, 0));
                        } catch (RemoteException e) {
                            // The client is dead.  Remove it from the list;
                            // we are going through the list from back to front
                            // so this is safe to do inside the loop.
                            mClients.remove(i);
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }



##3.注意事项：


###3.1使用 classLoader的问题

1.1关于Parcelable中我们说：
  在使用budler的时候请务必通过调用 Bundle.setClassLoader(ClassLoader) 设置软件包的类加载器。否则，即使您在应用中正确定义 Parcelable类型，也会遇到 ClassNotFoundException

    bundle.setClassLoader(getClass().getClassLoader());
    Rect rect = bundle.getParcelable("rect");
    process(rect); // Do more with the parcelable.

  
 [通过 IPC 传递对象](https://developer.android.google.cn/guide/components/aidl#PassingObjects)
 您可以通过 IPC 接口，将某个类从一个进程发送至另一个进程。不过，您必须确保 IPC 通道的另一端可使用该类的代码，并且该类必须支持 Parcelable 接口。支持 Parcelable 接口很重要，因为 Android 系统能通过该接口将对象分解成可编组至各进程的基本对象。
 
 如要创建支持 Parcelable 协议的类，您必须执行以下操作：
 
- 让您的类实现 Parcelable 接口。
- 实现 writeToParcel，它会获取对象的当前状态并将其写入 Parcel。
- 为您的类添加 CREATOR 静态字段，该字段是实现 Parcelable.Creator 接口的对象。
> 最后，创建声明 Parcelable 类的 .aidl 文件（遵照下文 Rect.aidl 文件所示步骤）。
> 如果您使用的是自定义编译进程，请勿在您的构建中添加 .aidl 文件。此 .aidl 文件与 C 语言中的头文件类似，并未经过编译。

 AIDL 会在其生成的代码中使用这些方法和字段，以对您的对象进行编组和解编。
 
 例如，下方的 Rect.aidl 文件可创建 Parcelable 类型的 Rect 类：

	 package android.graphics;
	 
	 // Declare Rect so AIDL can find it and knows that it implements
	 // the parcelable protocol.
	 parcelable Rect;
	 
在aidl的时候，编译器已经帮我们检查了，而且代码是最新生成的，属于同一个加载器，进而没有这么多的问题

 todo 父类委托机制添加解析

  
  
  为什么要使用classforename带加载器的，而不是不带的，这集考虑到父类加载机制了
  
 
 
###3.2使用 AIDL与当前Parcelable实现类的位置要求比较


在不同的apk中，要求我们所传递的Parcelable实现类，限定名(包名+类名)要完全一致


    Bundle bundle = new Bundle();
    bundle.putParcelable("msg", new Student("zyy"));

  
    public void putParcelable(@Nullable String key, @Nullable Parcelable value) {
         unparcel();
         mMap.put(key, value);
         mFlags &= ~FLAG_HAS_FDS_KNOWN;
     }
从上面的代码，我们似乎也没有看见，最后的处理过程，具体的回调等方法，那我们继续往下看：
看一下Bundle方法会发现，他也是一个Parcelable对象：

	public final class Bundle extends BaseBundle implements Cloneable, Parcelable

我们可以看看他的writeToParcel是如何实现的

	Bundle#writeToParcel
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        final boolean oldAllowFds = parcel.pushAllowFds((mFlags & FLAG_ALLOW_FDS) != 0);
        try {
            super.writeToParcelInner(parcel, flags);
        } finally {
            parcel.restoreAllowFds(oldAllowFds);
        }
    }

 	BaseBundle#writeToParcelInner
    void writeToParcelInner(Parcel parcel, int flags) {
		//省掉了部分代码
        if (map == null || map.size() <= 0) {
            parcel.writeInt(0);
            return;
        }
        int lengthPos = parcel.dataPosition();
        parcel.writeInt(-1); // dummy, will hold length
        parcel.writeInt(BUNDLE_MAGIC);

        int startPos = parcel.dataPosition();
        //重点代码
        parcel.writeArrayMapInternal(map);
        int endPos = parcel.dataPosition();

        // Backpatch length
        parcel.setDataPosition(lengthPos);
        int length = endPos - startPos;
        parcel.writeInt(length);
        parcel.setDataPosition(endPos);
    }

	Parcel#writeValue
	public final void writeValue(Object v) {
	      if (v instanceof Parcelable) {
	            // IMPOTANT: cases for classes that implement Parcelable must
	            // come before the Parcelable case, so that their specific VAL_*
	            // types will be written.
	            writeInt(VAL_PARCELABLE);
	            writeParcelable((Parcelable) v, 0);
	        } 
	 }

	 Parcel#writeParcelable
	 public final void writeParcelable(Parcelable p, int parcelableFlags) {
	     if (p == null) {
	         writeString(null);
	         return;
	     }
	     writeParcelableCreator(p);
	     //回调我们自己的写法
	     p.writeToParcel(this, parcelableFlags);
	 }
  
 	 Parcel#writeParcelableCreator
	  //限定了类的信息(包名+类名字)
	  /** @hide */
	  public final void writeParcelableCreator(Parcelable p) {
	      String name = p.getClass().getName();
	      writeString(name);
	  }  
  
  
	获取

    Bundle bundle = new Bundle();
    bundle.getParcelable("student");
  
        
     在跨进程传递时候，应该是从这个地方产生的数据
     BaseBundle(Parcel parcelledData) {
            readFromParcelInner(parcelledData);
     }
    
        BaseBundle(Parcel parcelledData, int length) {
            readFromParcelInner(parcelledData, length);
        }


    /* package */ void readArrayMapInternal(ArrayMap outVal, int N,
         Object value = readValue(loader);
    }
    
    public final <T extends Parcelable> T readParcelable(ClassLoader loader) {
        Parcelable.Creator<?> creator = readParcelableCreator(loader);
        if (creator == null) {
            return null;
        }
        if (creator instanceof Parcelable.ClassLoaderCreator<?>) {
          Parcelable.ClassLoaderCreator<?> classLoaderCreator =
              (Parcelable.ClassLoaderCreator<?>) creator;
          return (T) classLoaderCreator.createFromParcel(this, loader);
        }
        //回调我们的方法
        return (T) creator.createFromParcel(this);
    }


    /** @hide */
    public final Parcelable.Creator<?> readParcelableCreator(ClassLoader loader) {
        String name = readString();
        if (name == null) {
            return null;
        }
        Parcelable.Creator<?> creator;
        synchronized (mCreators) {
            HashMap<String,Parcelable.Creator<?>> map = mCreators.get(loader);
            if (map == null) {
                map = new HashMap<>();
                mCreators.put(loader, map);
            }
            creator = map.get(name);
            if (creator == null) {
                try {
                    // If loader == null, explicitly emulate Class.forName(String) "caller
                    // classloader" behavior.
                    ClassLoader parcelableClassLoader =
                            (loader == null ? getClass().getClassLoader() : loader);
                    // Avoid initializing the Parcelable class until we know it implements
                    // Parcelable and has the necessary CREATOR field. http://b/1171613.
                    Class<?> parcelableClass = Class.forName(name, false /* initialize */,
                            parcelableClassLoader);
                    if (!Parcelable.class.isAssignableFrom(parcelableClass)) {
                        throw new BadParcelableException("Parcelable protocol requires subclassing "
                                + "from Parcelable on class " + name);
                    }
                    Field f = parcelableClass.getField("CREATOR");
                    if ((f.getModifiers() & Modifier.STATIC) == 0) {
                        throw new BadParcelableException("Parcelable protocol requires "
                                + "the CREATOR object to be static on class " + name);
                    }
                    Class<?> creatorType = f.getType();
                    if (!Parcelable.Creator.class.isAssignableFrom(creatorType)) {
                        // Fail before calling Field.get(), not after, to avoid initializing
                        // parcelableClass unnecessarily.
                        throw new BadParcelableException("Parcelable protocol requires a "
                                + "Parcelable.Creator object called "
                                + "CREATOR on class " + name);
                    }
                    creator = (Parcelable.Creator<?>) f.get(null);
                }
                catch (IllegalAccessException e) {
                    Log.e(TAG, "Illegal access when unmarshalling: " + name, e);
                    throw new BadParcelableException(
                            "IllegalAccessException when unmarshalling: " + name);
                }
                catch (ClassNotFoundException e) {
                    Log.e(TAG, "Class not found when unmarshalling: " + name, e);
                    throw new BadParcelableException(
                            "ClassNotFoundException when unmarshalling: " + name);
                }
                catch (NoSuchFieldException e) {
                    throw new BadParcelableException("Parcelable protocol requires a "
                            + "Parcelable.Creator object called "
                            + "CREATOR on class " + name);
                }
                if (creator == null) {
                    throw new BadParcelableException("Parcelable protocol requires a "
                            + "non-null Parcelable.Creator object called "
                            + "CREATOR on class " + name);
                }

                map.put(name, creator);
            }
        }

        return creator;
    }


  一些列的检查，排查类的存在等条件，在这个地方出现了类加载器的概念
  
   
       Bundle data = msg.getData();
       或者
       Bundle data = msg.obj;
       都不会报错，是因为Bundle 在系统加载器的范围内，但是我们新定义的类，不在那个范围内导致
       
       
  Bundle bundle = new Bundle();
  bundle.putParcelable("msg", new Student("zyy"));
  msg.obj = new Student("zyy");   
  
  如果通过这种方式，不管如何，我们得到的代码都会报错
  
  
  

 
  
  


https://developer.android.google.cn/guide/components/bound-services?hl=zh-cn#Messenger


https://developer.android.google.cn/guide/components/bound-services?hl=zh-cn
  
  