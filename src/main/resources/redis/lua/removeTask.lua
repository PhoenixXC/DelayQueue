---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by xuanc.
--- DateTime: 2020/2/3 上午10:54
--- Description: 移除任务
---

local taskKey = KEYS[1];
local waitingKey = KEYS[2];

-- in taskKey
local remField = ARGV[1];
-- in waitingKey
local remValue = ARGV[2];

-- del
local taskDetail;
taskDetail = redis.call('HGET', taskKey, remField);
redis.call('HDEL', taskKey, remField)
redis.call('ZREM', waitingKey, remValue)

return taskDetail

