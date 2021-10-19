package EngineSkill.MultiThread;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


class Task implements Runnable{
    private final String taskName;
    public Task(String name){
        this.taskName=name;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName()+" StartTask==>"+taskName);
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){

        }
        System.out.println(Thread.currentThread().getName()+" FinishTask==>"+taskName);
    }
}




public class MyThreadPool {

    private static Logger logger = LoggerFactory.getLogger(MyThreadPool.class);

    public static void main(String[] args){


        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);//创建固定大小的线程池

        /**在指定范围创建线程数量**/
/*        int minT=20;int maxT=50;
        ExecutorService executorService = new ThreadPoolExecutor(
                minT,
                maxT,
                60L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>()
        );*/


        for(int i=0;i<19;i++){
            executor.execute(new Task(""+i));
            logger.info("线程池中线程数目="+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                    executor.getQueue().size()+"，已执行别的任务数目："+executor.getCompletedTaskCount());
        }
        //关闭线程池
        //executorService.shutdown();



    }














}
