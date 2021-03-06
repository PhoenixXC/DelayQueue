package top.xcphoenix.delayqueue.monitor.init.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import top.xcphoenix.delayqueue.constant.RedisDataStruct;
import top.xcphoenix.delayqueue.monitor.global.GroupMonitor;
import top.xcphoenix.delayqueue.monitor.global.TopicMonitor;
import top.xcphoenix.delayqueue.monitor.init.InitScanner;

import java.util.Arrays;
import java.util.Set;

/**
 * 扫描 TOPIC
 *
 * @author xuanc
 * @version 1.0
 * @date 2020/2/6 下午10:31
 */
@Component("scan-topic")
@Order(2)
@Slf4j
public class TopicInitScanImpl implements InitScanner {

    private GroupMonitor groupMonitor;
    private TopicMonitor topicMonitor;
    private StringRedisTemplate redisTemplate;

    public TopicInitScanImpl(GroupMonitor groupTopicMonitor, TopicMonitor topicMonitor, StringRedisTemplate redisTemplate) {
        this.groupMonitor = groupTopicMonitor;
        this.topicMonitor = topicMonitor;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args) {
        log.info("Topic init...");

        String monitorKey = RedisDataStruct.PROJECT_MONITOR_KEY;
        Set<String> groups = groupMonitor.getMonitoredGroups();

        for (String group : groups) {
            // get topics
            String topics = (String) redisTemplate.opsForHash().get(monitorKey, group);
            String[] topicArr = topics == null ? new String[0]
                            : topics.split(RedisDataStruct.MONITOR_TOPIC_DELIMITER);

            log.info("Init group: " + group + ", topics: " + Arrays.toString(topicArr));

            // init
            topicMonitor.init(group);
            for (String topic : topicArr) {
                topicMonitor.pushNewTopic(group, topic);
            }
        }

    }

}
