package top.xcphoenix.delayqueue.pojo;

/**
 * @author      xuanc
 * @date        2020/2/9 下午9:20
 * @version     1.0
 */ 
public interface Callback {

    /**
     * 要执行的任务
     *
     * @throws Exception 实现类运行产生的异常
     */
    void call() throws Exception;

}
