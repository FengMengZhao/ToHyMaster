# 数据库基础(2017-Q4) & SQL基础

## 数据库基础

**范式:**

- 第一范式: 指数据库表的每一列都是不可分割的基本数据项.
- 第二范式: 数据库表中不存在非关键字段对任一候选键的部分函数依赖，也即所有非关键字段都完全依赖于任意一组候选关键字。
- 第三范式: 在第二范式的基础上，数据表中如果不存在非关键字段对任一候选关键字段的传递函数依赖则符合第三范式.

**事务:**

事务是一系列的数据库操作,是数据库应用的基本单元,是反映现实世界需要以完整单位提交的一项工作.事务是用户定义的一个数据库操作序列.

事务特性:

- ACID
    - 原子性(Atomicity)
        - 整个事务中的所有操作,要么全部完成,要不全部不完成,不能停滞在中间的某个环节
            - 执行过程中会回滚(Rollback)到事务开始前的状态,就像事务从来没有执行一样
    - 一致性(Consistency)
        - 保持系统处于一致状态(事务执行前和执行后处于一致的状态)
            - 如果事务是并发多个,也必须如同串行事务一样操作
    - 隔离性(Isolation)
        - 隔离状态执行事务,好像是系统在给定时间内执行的唯一操作
        - 不考虑隔离性会发生的问题
            - 脏读(一个事务处理的过程中读取了另一个未提交事务中的数据)
            - 不可重复读(一个事务处理的过程中读取了另一个已经提交事务中的数据)
            - 幻读(和不可重复读类似，区别是幻读针对的是一批数据整体，例如数据的个数)
    - 持久性
        - 事务执行完成后,不发生回滚

**数据库约束的5中形式:**

1. NOT NULL(非空)
2. PRIMARY KEY(主键)
3. UNIQUE KEY(唯一)
4. DEFAULT(默认值)
5. FOREIGN KEY(主键)

**完整性与一致性:**

完整性(Integrity): 更多的是针对业务来说的.完整性包括实体完整性和参照完整性.实体完整性是通过主键约束,参照完整性是通过外键约束.另外用户可以自定义完整性约束.

一致性(Consistency): 是事务的一个特性,要底层一点.要么执行完成,要么不执行,保持数据的一致性.

**数据库并发控制:**

- 悲观并发控制机制(Pessimistic Concurrency Control)-锁
- 乐观并发控制机制(Optimistic Concurrency Control)-乐观锁
    - 基于时间戳的协议
    - 基于验证的协议
- 多版本并发控制(Multiversion Concurrency Control)-
    - 每一个写操作都会创建一个新版本的数据
    - 读操作会从有限的多版本中挑选一个最为何时的结果直接返回

---
