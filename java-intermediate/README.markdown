# Java中级(2017-Q4)

## 目录

- [Heap dump Anylysis](#1)
    - [内存溢出实例](#1.1)
    - [通过工具进行Heap dump](#1.2)
    - [Java hasCode and equals method](#1.3)
- [Java多线程](#2)

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
