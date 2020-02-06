package top.xcphoenix.delayqueue.threads;

import lombok.extern.slf4j.Slf4j;
import top.xcphoenix.delayqueue.service.DelayQueueService;
import top.xcphoenix.delayqueue.utils.BeanUtil;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author      xuanc
 * @date        2020/2/5 下午5:24
 * @version     1.0
 */
@Slf4j
public class PushTaskThread implements Runnable {

    /** 关注的 group    */
    private String attentionGroup;
    /** 下一次操作的时间  */
    private AtomicLong nextTime = new AtomicLong(0);
    /** get delayqueue for push */
    private DelayQueueService delayQueueService = BeanUtil.getBean(DelayQueueService.class);

    public PushTaskThread(@NotNull String attentionGroup) {
        this.attentionGroup = attentionGroup;
    }

    @Override
    public synchronized void run() {
        log.info("PushTask:: start monitor for push task, attend group => " + attentionGroup);

        while (!Thread.currentThread().isInterrupted()) {
            long now = System.currentTimeMillis();

            if (nextTime.get() <= now) {
                // push processing
                Long newTime = delayQueueService.pushTask(attentionGroup, System.currentTimeMillis(), nextTime.get());
                // update nextTime
                // TODO 如果等待队列为空，是否继续wait下去...
                nextTime.set(Objects.requireNonNullElse(newTime, Long.MAX_VALUE));

                log.info("next exec time: " + nextTime.get());

            } else {
                log.info("wait until time is go");

                try {
                    // 阻塞
                    wait(nextTime.get() - now);
                } catch (InterruptedException e) {
                    break;
                }

                log.info("wait end");
            }
        }
        log.info("PushTask:: end push task, attend group => " + attentionGroup);
    }

}
