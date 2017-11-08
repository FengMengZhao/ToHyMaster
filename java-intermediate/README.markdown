# Java中级(2017-Q4)

## 目录

- [1. Heap dump Anylysis](#1)
    - [1.1 内存溢出实例](#1.1)
    - [1.2 通过工具进行Heap dump](#1.2)
    - [1.3 Java hasCode and equals method](#1.3)
- [2. Java多线程](#2)
    - [2.1 进程 VS 线程](#2.1)
    - [2.2 线程对象](#2.2)
    - [2.3 同步(Synchronization)](#2.3)
    - [2.4 死锁](#2.4)
    - [2.5 Guarded Block](#2.5)

---

<h3 id = "1">Heap dump Analysis</h3>

Java应用程序要求使用大小有限的共享内存空间,这个限制可以在程序启动的时候指定.为了更加方便应用,逻辑又将之分为堆内存(Heap Space)和方法区(Permanent Generation or Mothod Area).

Java共享内存区域的大小可以在JVM启动的时候可以设定,如果不显示的设定参数值,JVM会使用默认值.

- JVM 默认Heap Size查看方法:
- `java -XX:+PrintFlagsFinal -version | findstr HeapSize`
- `java -XX:+PrintFlagsFinal -version | grep HeapSize`
- 常用JVM启动参数:
- `-Xms <heap size> [g|m|k] -Xmx <heap size> [g|m|k]`
- `-XX:PermSize=<per gen size> [g|m|k] -XX:MaxPermSize=<perm gen size> [g|m|k]`
- `-Xmn <young size> [g|m|k]`
- `-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=<output file>.hprof`
- `java -XX:+PrintFlagsFinal -version`

当Java应用程序试图在堆上申请更多的空间,但是设定的内存空间不足时就会引起`java.lang.OutOfMemoryError`.

> 物理机的内存空间可能是充足的,但是当堆内存达到上限的时候就会抛出`java.lang.outOfMemoryError`.

正常情况下,当Java应用程序运行需要比堆上限更对的内存的时候,就会出现异常.这个时候,我们需要重新设定JVM的Heap Space limit.同时,由于程序上的一些错误或者复杂的情况下,也可能发生内存溢出的异常:

- 程序的使用量或者数据量出现峰值
- 内存泄漏(Memory Leak)

<h4 id = "1.1">内存溢出的实例</h4>

**堆内存限制导致内存溢出**

    class OMM{

        static final int size = 2*1024*1024;
        public static void main(String args[]){
            int[] i = new int[size];
        }
    }

> `java OMM -Xmx12m` 会导致内存溢出;`java OMM -Xmx13m`则不会出现内存溢出.

**内存泄漏导致内存溢出**

    import java.util.*;

    public class MemoryLeak{

        static class Key{
            Integer id;

            Key(Integer id){
                this.id = id;
            }

            @Override
            public int hashCode(){
                return id.hashCode();
            }

            /*
            @Override
            public boolean equals(Object o){
                boolean response = false;
                if(o instanceof Key){
                    response = (((Key)o).id).equals(this.id);
                }
                return response;
            }
            */
        }

        public static void main(String args[]){
            Map<Key, String> m = new HashMap<Key, String>();

            while(true){
                for(int i = 0; i < 10000; i++){
                    if(!m.containsKey(new Key(i))){
                        m.put(new Key(i), "Nuumber: " + i);
                    }
                }
            }
        }
    }

- 程序中如果Key类只复写了`hashCode()`方法,而没有`equals()`方法,则程序会无限制的申请Key对象,直到内存溢出.
- 内存泄漏: 一些程序不再使用的对象不能够被JVM GC识别,而无法回收.上述的内存泄漏问题可以通过复写`equal()`方法解决.

<h4 id="1.2">通过工具进行Heap Dump</h4>

查看并分析Java Heap Space有很多方法:

- `jmap(java Memory Map),jhat(java Heap analysis tool)`
- `jvisualvm`
- `Plumbr`
- `IBM HeapAnalyzer`
- `Eclipse MAT(Memory Analysis Tool)`

**通过Jmap和jhat分析堆内存**

- `jps -lm`: 列出正在运行的Java进程和进程号
    - `jps`工具在jdk1.6的版本中有
- `jmap -dump:format=b,file=<some_file>.bin <java_process_num>`
    - 命令格式可以通过`jmap -help`得到
- `jhat <some_file>.bin`
    - 启动一个本地服务:`http://127.0.0.1:7000`,访问即可

**通过jvisualvm分析堆内存**

- 正在运行的应用
    - `jvisualvm`: 自动监控本地的Java应用
        - 右键java进程,选择Heap Dump可以查看堆内存实时状态
- 执行结束的应用
    - `java <java_app> -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=<output file>.hprof`
    - `jvisualvm`: 将上述文件装入,即可查看运行结束的Java应用堆内存

<h4 id = "1.3">Java hasCode and equals method</h4>

- `equals`方法
    - 对于任意的非`null`引用x,`x.equals(x)`应该放回`true`
    - 对于任意的非`null`引用x y,如果`x.equals(y)`放回true,`y.equals(x)`也应该返回`true`
    - 对于任意的非`null`引用x y,如果`x.equals(y)`为`true`,`y.equals(z)`为`true`,则`x.equals(z)`应该返回`true`
    - 多次调用的返回结果应该保持一致性
    - 对于任意非`null`引用x,`x.equals(null)`应该返回false
- `hashCode`方法
    - 在一次的Java运行过程中,一个对象的`hashCode`的返回结果应该保持一致性;多个Java运行实例中一个对象的`hashCode`结果没必要保持一致性.
    - 如果两个对象equals,则这两个对象必须有相等的hashCode
    - 如果两个对象有相等的hashCode,则这两个对象未必equals;也就是说非equals的对象,也可能有相等的hashCode
        - 虽然不equals的对象不要求有不同的hashCode,但如果不同对象对应不同的hashCode,则能够提供性能

**覆写equals方法必须覆写hashCode方法**

    import java.util.*;

    public class Apple{

        private String color;    

        public Apple(String color){
            this.color = color;
        }

        @Override
        public boolean equals(Object obj){
            if(null == obj){
                return false;
            }
            if(! (obj instanceof Apple)){
                return false;
            }
            if(obj == this){
                return true;
            }
            return color.equals(((Apple)obj).color);
        }

        /*
        @Override
        public int hashCode(){
            return color.hashCode();
        }
        */

        public static void main(String args[]){

            Map<Apple, Integer> appleMap = new HashMap<Apple, Integer>();
            appleMap.put(new Apple("red"), 1);
            appleMap.put(new Apple("green"), 2);
            System.out.println(appleMap.get(new Apple("green")));
        }
    }

---

<h3 id="2">Java多线程</h3>

<h4 id="2.1">进程 VS 线程</h4>

**进程:**

一个进程有独立的运行环境.通常情况下,一个进程具有完全私有的基本运行时资源.尤其是,每一个进程有自己独立的内存.

进程也通常被称为程序或应用的代名词.然而,人们普遍认为的进程实际上是相互合作的进程集.操作系统通过管道和套接字等IPC(Inter Process Communication)资源实现同一个系统或不同系统间的交流协作.

**线程:**

线程被称为轻量级的进程.线程和进程都提供程序运行环境,相对于进程,但是创建一个新的线程需要更少的资源.

线程依附于进程存在:每一个进程都至少有一个进程.线程分享进程的资源,包括内存和打开文件.这使得共同更加有效,同时也带来潜在的问题.

Java支持多线程环境是必要的.如果你认为内存管理和信号处理是一个线程,每一个Java应用都至少有一个或者多个线程.但是从开发者的角度,你仅仅从一个线程开始,这个线程是主线程.这个主线程有能力创建其他线程.

<h4 id="2.2">线程对象</h4>

使用Thread Object的两种方式:

- 手动创建线程对象
    - extends Thread 
    - new Thread(new Runnable)
- 将线程的管理抽象,将应用的tasks创递给一个executor

Thread主要方法:

- java.lang.Thread
    - class method
        - Thread.currentThread()
        - Thread.sleep()
        - Thread.interrupted(): 当前线程是否被interrupted
        - Threa.yield(): 当前线程暗示可以交出CPU
    - instance method
        - new Thread().isInterrupted(): this线程是否被interrupted
        - new Thread().interrupt(): interrupt this线程
        - new Thread().join: 当前线程等待this线程,直到this线程结束

**join()方法示例:**

    public class ThreadJoin{
        private static class PrintInteger implements Runnable{
            
            @Override
            public void run(){
                for(int i = 0; i < 5; i++){
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " --> " + (i+1));
                }
            }
        }

        public static void main(String args[]){

            Thread t1 = new Thread(new PrintInteger());
            Thread t2 = new Thread(new PrintInteger());
            Thread t3 = new Thread(new PrintInteger());
            t1.start();
            try{
                t1.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            t2.start();
            t3.start();
        }
    }

**使用join让多线程顺序执行:**

    public class ThreadSequence{
        private static class PrintInteger implements Runnable{
            
            @Override
            public void run(){
                for(int i = 0; i < 5; i++){
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " --> " + (i+1));
                }
            }
        }

        public static void main(String args[]){

            Thread t1 = new Thread(new PrintInteger());
            Thread t2 = new Thread(new PrintInteger());
            Thread t3 = new Thread(new PrintInteger());
            t1.start();
            try{
                t1.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            t2.start();
            try{
                t2.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            t3.start();
        }
    }

<h4 id="2.3">同步(Synchronization)</h4>

线程间的交流主要是通过共享域和引用对象的访问完成.这种方式的交流非常有效,同时也会带来两种方面的问题:**线程干扰(thread interference)**和**内存一致性(memory consistency errors)**.要解决这些问题,就需要用到同步.

当两个或者多个线程同时访问相同的资源的时候,同步会引起线程竞争(thread contention),这样会导致JVM执行线程速度变慢,甚至挂起运行.饿死(Stavation)和活锁(livelock)是两种常见的线程竞争的形式.

Java提供了两种基本的同步用法:

- synchronize method
- synchronize statement

Synchronized同步方法能够阻止线程干扰和内存一致性的问题.

**内置锁(intrinsic lock)**

每一个对象都有一个内置锁与之相关联.通常,一个线程如果想长期独占对一个对象的访问,则这个线程必须首先获得这个对象的内置锁,并且在结束的时候释放锁.从线程获取一个对象的锁开始到释放这个锁的过程叫做:这个对象拥有这个锁.只要一个线程拥有一个对象的内置锁,其他线程没有办法或者该对象的内置锁,而试图获取该对象内置锁的线程就会处于block状态.

**原子访问(Atomic Access)**

在程序中,原子操作一次性执行完成: 原子操作不会中途停止,要么完全执行,要么没有执行.

常见原子操作:

- 对引用变量及大部分基本数据类型(long,double除外)的读和写操作是原子性的
- 多所有`volatile`修饰的所有变量(包括long,double)的读和写操作是原子性的

<h4 id="2.4">死锁(deadlock)</h4>

**死锁示例:**

    public class DeadLock{

        static class Friend{
            private String name;

            public Friend(String name){
                this.name = name;
            }

            public String getName(){
                return name;
            }
            
            public synchronized void bow(Friend another){
                System.out.println(name + "向" + another.getName() + "鞠躬.");
                another.bowBack(this);
            }

            public synchronized void bowBack(Friend another){
                System.out.println(name + "向" + another.getName() + "回礼.");
            }
        }

        public static void main(String args[]){
            final Friend f1 = new Friend("张三");
            final Friend f2 = new Friend("李四");
            new Thread(new Runnable(){
                public void run(){
                    f1.bow(f2);
                }
            }).start();
            new Thread(new Runnable(){
                public void run(){
                    f2.bow(f1);
                }
            }).start();
        }
    }

> 张三和李四是懂礼貌的好朋友,当他们碰到对方时要向对方鞠躬并且直到对方做出回应方可起身.可是,当他们同时向对方鞠躬的时候,死锁就发生了.

> 要尽量避免在同步块中执行执行其他对象的方法,以为这样容易引起Liveness问题.

- Liveness
    - 饿死(Starvation): 当一个线程不能够获得对共享数据的访问,并且不能有所进展的情形.
    - 活锁(Livelock): 当一个线程回应另一个线程的同时,另一个线程同时回应前一个线程.

> 活锁和死锁一样,程序不能够继续执行,但不同的是活锁中的线程处于繁忙状态,并没有处于block状态.

<h4 id="2.5">Guarded Block</h4>

线程之间需要协作对方的行为.最常用的一种协作方式是`Guarded Block`,这个block循环一个条件,当这个条件为`true`时,block才能继续执行.

    public void guardedJoy() {
        // Simple loop guard. Wastes
        // processor time. Don't do this!
        while(!joy) {}
        System.out.println("Joy has been achieved!");
    }

这样的代码虽然满足了`Guarded Block`的要求,但是浪费资源.在`while`循环中,线程在等待时还一直处于工作状态(消耗CPU).

通常我们用同步中`wait`和`notify`解决上述问题:

    public synchronized void guardedJoy() {
        // This guard only loops once for each special event, which may not
        // be the event we're waiting for.
        while(!joy) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        System.out.println("Joy and efficiency have been achieved!");
    }

> 为什么`wait`和`notify`是`java.lang.Object`中的方法,而不放入`java.lang.Thread`中?这是因为: 不同的线程是通过获取对象的内置锁的方式来进行独占访问的;对象在线程间进行共享,线程彼此之间并不知道对方的信息,通过对象锁状态的改变可以让线程等待和通知线程竞争,而一个线程无法通知另一个线程.举例来说:火车上的人上厕所,当人发现指示灯是绿色的时候会尝试开门进入厕所,而指示灯是红色的时候,会坐回原位置等待.这里的人相当于线程,厕所相当于对象,指示灯相当于对象的内置锁.指示灯颜色的转变是通过厕所(对象)门反锁与否来完成的,是对象发出的行为,而不是线程(人)发出的行为.

---
