---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by xuanc.
--- DateTime: 2020/2/7 下午3:16
--- Description: 从topic队列中获取任务
---

-- function: log
local function log(str)
    redis.log(redis.LOG_VERBOSE, '[LUA] ' .. str);
end

local consumingKey = KEYS[1]
local taskKey = KEYS[2]

-- ARGV[1] 为获取的数量
-- ARGV[1] == 0 会获取所有数据
local number = ARGV[1] - 1

log("EVAL lua [getTasksInList] ---------------------------")
log("consumingKey: " .. consumingKey ..", taskKey: " .. taskKey .. ", number: " .. ARGV[1])

-- get task list
log("get task ids from " .. consumingKey)
local result = redis.call('LRANGE', consumingKey, 0, number);
if result == nil or #result == 0 then
    log("result is nil")
    return nil;
end
log("result:");
for k,v in ipairs(result) do
    log("~ result[" .. k .. "] = " .. v);
end

-- remove tasks in consumingKey
log("remove tasks in " .. consumingKey)
redis.call('LTRIM', consumingKey, number + 1, -1);

-- get tasks from detail hash
log("get tasks from " .. taskKey)
local tasks = redis.call('HMGET', taskKey, unpack(result));
if type(tasks)=="table" then
    for k,v in ipairs(tasks) do
        log("~ task[" .. k .. "] = " .. v);
    end
end

-- remove tasks in detail hash
log("remove tasks in " .. taskKey)
redis.call('HDEL', taskKey, unpack(result));

return tasks;