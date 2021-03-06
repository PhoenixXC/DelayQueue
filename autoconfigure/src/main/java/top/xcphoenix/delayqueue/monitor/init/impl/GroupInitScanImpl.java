package top.xcphoenix.delayqueue.monitor.init.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import top.xcphoenix.delayqueue.constant.RedisDataStruct;
import top.xcphoenix.delayqueue.monitor.global.GroupMonitor;
import top.xcphoenix.delayqueue.monitor.init.InitScanInterface;

import java.util.Set;

/**
 * 扫描 group
 *
 * @author      xuanc
 * @date        2020/2/5 下午4:35
 * @version     1.0
 */
@Slf4j
public class GroupInitScanImpl implements InitScanInterface {

    private GroupMonitor groupMonitor;
    private RedisTemplate redisTemplate;

    public GroupInitScanImpl(GroupMonitor groupMonitor, RedisTemplate redisTemplate) {
        this.groupMonitor = groupMonitor;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args) {
        log.info("Group init...");

        String redisKey = RedisDataStruct.PROJECT_MONITOR_KEY;
        Set<Object> groups = redisTemplate.opsForHash().keys(redisKey);

        log.info("Init groups: " + groups.toString());

        for (Object grp : groups) {
            String group;
            if (grp instanceof String) {
                group = (String) grp;
            } else {
                group = grp.toString();
            }
            groupMonitor.pushNewGroup(group);
        }
    }

}
