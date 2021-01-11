


使用 Messenger
如需让接口跨不同进程工作，您可以使用 Messenger 为服务创建接口。采用这种方式时，服务会定义一个 Handler，用于响应不同类型的 Message 对象。
此 Handler 是 Messenger 的基础，后者随后可与客户端分享一个 IBinder，以便客户端能利用 Message 对象向服务发送命令。
此外，客户端还可定义一个自有 Messenger，以便服务回传消息。
这是执行进程间通信 (IPC) 最为简单的方式，因为 Messenger 会在单个线程中创建包含所有请求的队列，这样您就不必对服务进行线程安全设计。



大多数应用不应使用 AIDL 来创建绑定服务，因为它可能需要多线程处理能力，并可能导致更为复杂的实现。
因此，AIDL 并不适合大多数应用，本文也不会阐述如何将其用于您的服务。如果您确定自己需要直接使用 AIDL，请参阅 AIDL 文档。

https://developer.android.google.cn/guide/components/bound-services?hl=zh-cn#Messenger


使用 Messenger
如果您需要让服务与远程进程通信，则可使用 Messenger 为您的服务提供接口。借助此方式，您无需使用 AIDL 便可执行进程间通信 (IPC)。

为接口使用 Messenger 比使用 AIDL 更简单，因为 Messenger 会将所有服务调用加入队列。纯 AIDL 接口会同时向服务发送多个请求，那么服务就必须执行多线程处理。

对于大多数应用，服务无需执行多线程处理，因此使用 Messenger 可让服务一次处理一个调用。如果您的服务必须执行多线程处理，请使用 AIDL 来定义接口。

以下是对 Messenger 使用方式的摘要：

服务实现一个 Handler，由其接收来自客户端的每个调用的回调。
服务使用 Handler 来创建 Messenger 对象（该对象是对 Handler 的引用）。
Messenger 创建一个 IBinder，服务通过 onBind() 将其返回给客户端。
客户端使用 IBinder 将 Messenger（它引用服务的 Handler）实例化，然后再用其将 Message 对象发送给服务。
服务在其 Handler 中（具体而言，是在 handleMessage() 方法中）接收每个 Message。
这样，客户端便没有调用服务的方法。相反，客户端会传递服务在其 Handler 中接收的消息（Message 对象）。

下面这个简单的服务实例展示了如何使用 Messenger 接口：





