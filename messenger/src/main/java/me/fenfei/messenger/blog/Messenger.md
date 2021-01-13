

https://developer.android.google.cn/guide/components/bound-services?hl=zh-cn#Messenger

为接口使用 Messenger 比使用 AIDL 更简单，因为 Messenger 会将所有服务调用加入队列。纯 AIDL 接口会同时向服务发送多个请求，那么服务就必须执行多线程处理。
对于大多数应用，服务无需执行多线程处理，因此使用 Messenger 可让服务一次处理一个调用。如果您的服务必须执行多线程处理，请使用 AIDL 来定义接口。

大多数应用不应使用 AIDL 来创建绑定服务，因为它可能需要多线程处理能力，并可能导致更为复杂的实现。

Messenger是执行进程间通信 (IPC) 最为简单的方式，因为 Messenger 会在单个线程中创建包含所有请求的队列，这样您就不必对服务进行线程安全设计。

##使用 Messenger

如需让接口跨不同进程工作，您可以使用 Messenger 为服务创建接口。采用这种方式时，服务会定义一个 Handler，用于响应不同类型的 Message 对象。
此 Handler 是 Messenger 的基础，后者随后可与客户端分享一个 IBinder，以便客户端能利用 Message 对象向服务发送命令。
此外，客户端还可定义一个自有 Messenger，以便服务回传消息。

使用 Messenger
如果您需要让服务与远程进程通信，则可使用 Messenger 为您的服务提供接口。借助此方式，您无需使用 AIDL 便可执行进程间通信 (IPC)。

以下是对 Messenger 使用方式的摘要：

1.服务实现一个 Handler，由其接收来自客户端的每个调用的回调。
2.服务使用 Handler 来创建 Messenger 对象（该对象是对 Handler 的引用）。
3.Messenger 创建一个 IBinder，服务通过 onBind() 将其返回给客户端。
4.客户端使用 IBinder 将 Messenger（它引用服务的 Handler）实例化，然后再用其将 Message 对象发送给服务。
5.服务在其 Handler 中（具体而言，是在 handleMessage() 方法中）接收每个 Message。

这样，客户端便没有调用服务的方法。相反，客户端会传递服务在其 Handler 中接收的消息（Message 对象）。
 之前的案例是add方法，在add方法中，我们是直接互动的

下面这个简单的服务实例展示了如何使用 Messenger 接口：


1. server
   static class IncomingHandler extends Handler{
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
       }
   }
   
2.server
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
    

3. client
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

4.client

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
  
     
5.如果想要双向消息传递：
  
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





