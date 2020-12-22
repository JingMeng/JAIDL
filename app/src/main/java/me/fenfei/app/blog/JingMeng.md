
###基本概念

1. 进程是程序的一个运行实例，以区别于"程序这一静态概念；而线程则是CPU调度的基本的单位。

2. 如果两个对象处于同一个进程空间中，那么内存区域应该是可以共享的(操作系统基本知识)<br/>
   也就是不同进程间内存区域是不共享的，并不是不同的"程序"
  
3. 同时在官网，我们可以找到下面这句话：
 > By default, all components of the same application run in the same process and most applications should not change this. However, if you find that you need to control which process a certain component belongs to, you can do so in the manifest file.
 [官网介绍地址](https://stuff.mit.edu/afs/sipb/project/android/docs/guide/components/processes-and-threads.html)
  
 上面的文档告诉我们，我们所创建的四大组件默认都在主线程中
 
 Tips：我们可以通过 android:process 来给Activity配置一个单独的进程。[官网介绍地址](https://developer.android.google.cn/guide/topics/manifest/activity-element?hl=zh-cn#proc)
  
4.基本验证









 
###问题的提出：
 


//
什么是aidl
aidl的作用
如何实现aidl