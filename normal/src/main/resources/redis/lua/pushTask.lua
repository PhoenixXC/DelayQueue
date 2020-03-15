---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by xuanc.
--- DateTime: 2020/2/1 下午3:32
--- Description: 推送任务
---

-- function: log
local function log(str)
    redis.log(redis.LOG_VERBOSE, '[LUA] ' .. str);
end

-- function: string split
local function split(str, reps)
    local resultStrList = {}
    string.gsub(str, '[^' .. reps .. ']+',
            function(w)
                table.insert(resultStrList, w);
            end
    )
    return resultStrList;
end

-- define
local waitingKey = KEYS[1]
local consumingKeyPrefix = KEYS[2]

local maxScore = ARGV[1]
local minScore = ARGV[2]

log("EVAL lua [pushTask] ---------------------------------")
log("keys: " .. waitingKey .. ", " .. consumingKeyPrefix);
log(string.format("minScore: %s, maxScore: %s", minScore, maxScore));

local status, type = next(redis.call('TYPE', waitingKey))
log("status: " .. status .. ", type: " .. type);

if status ~= nil and status == 'ok' then
    if type == 'zset' then
        -- get values
        local list = redis.call('ZRANGEBYSCORE', waitingKey, minScore, maxScore)

        if list ~= nil and #list > 0 then
            log("list length: " .. #list);

            for _, v in ipairs(list) do
                local item = split(v, ':');
                local topic = item[1];
                local taskId = item[2];
                local consumingKey = consumingKeyPrefix .. ':' .. topic;

                -- put consume list
                redis.call('RPUSH', consumingKey, taskId);
                log('~ add value ' .. taskId .. ' in key ' .. consumingKey);
            end

            -- remove from waiting zset
            redis.call('ZREM', waitingKey, unpack(list))
            log('delete list on waiting key');
        end
    end
end

return nil;