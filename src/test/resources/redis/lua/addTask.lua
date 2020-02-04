---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by xuanc.
--- DateTime: 2020/2/3 上午10:54
--- Description: 添加任务
---

-- 任务Hash
local taskKey = KEYS[1];
-- 等待队列
local waitingKey = KEYS[2];

-- field in taskKey
local hashField = ARGV[1]
-- 任务序列化对象
local taskObject = ARGV[2];
-- 有序集合值
local waitingValue = ARGV[3]
-- 任务执行时间
local taskExecTime = ARGV[4];

-- 放入任务Hash
redis.call('HSET', taskKey, hashField, taskObject);
-- 添加到等待队列
redis.call('ZADD', waitingKey, taskExecTime, waitingValue);

return nil