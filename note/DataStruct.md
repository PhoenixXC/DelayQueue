# 数据组织


1. 任务队列

   **类型**： `HASH`

   **名字**： `{GROUP}:DETAIL`

   - `{id 任务id}:{使用 Json 序列化任务对象}`

   > 加密校验对象防止恶意序列化？
   >
   > - 使用加密机制

2. 待消费队列

   **类型**： `ZSET`

   **名字**： `{GROUP}:WAITING`

   **值**：     

   - <kbd>value</kbd>`{topic}:{id}`
   - <kbd>score</kbd> `{execTime}`

   任务等待队列，使用任务执行时间作为分数

   定时启动、根据时间轮训判断是否有任务可以被消费

3. 消费队列

   放置要处理的任务，每次从队列中取出一定量的任务去处理，如果任务为空就阻塞。

   **类型**： `LIST`

   **名字**： `{GROUP}:CONSUMING:TOPIC`

   **值**：     `{id 任务id}`
   
   > 如果任务执行失败或发生（异常），重试。
   > - 指定重试次数
   > - 指定相应异常
   > - 警告机制


