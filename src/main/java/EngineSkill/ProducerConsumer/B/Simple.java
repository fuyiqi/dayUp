package EngineSkill.ProducerConsumer.B;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Simple {
    /**
     * 多生产者多消费者模式之使用单缓冲区
     *  缓冲区使用线性队列
     *  单个缓冲区在面对多生成多消费时，读写开销大
     */

    public static void main(String[] args){
        final Lock lock = new ReentrantLock();
        Condition producerCondition = lock.newCondition();
        Condition consumerCondition = lock.newCondition();
        Resource resource = new Resource(lock,consumerCondition,producerCondition);

        ProducerB pb1 = new ProducerB(resource);ProducerB pb2 = new ProducerB(resource);
        ConsumerB cb1 = new ConsumerB(resource);ConsumerB cb2 = new ConsumerB(resource);
        cb1.start();pb1.start();pb2.start();cb2.start();



    }


}

/**
 * 公共资源
 */

class Resource{
    private int availableNum=0;
    private int initCapacity=20;
    private Lock lock;
    private Condition triggerProducerCondition;
    private Condition triggerConsumerCondition;

    public Resource(Lock lock,Condition triggerConsumerCondition,Condition triggerProducerCondition){
        this.lock=lock;
        this.triggerConsumerCondition=triggerConsumerCondition;
        this.triggerProducerCondition=triggerProducerCondition;
    }


    public void remove(){
        lock.lock();
        try{
            if(availableNum>0){
                availableNum--;
                System.out.println("消费者:"+Thread.currentThread().getId()+"消费了一条资源，当前资源数量："+this.availableNum);
                triggerProducerCondition.signalAll();//唤醒等待的生产者
            }else {//资源池已空
                try{
                    triggerConsumerCondition.await();
                    System.out.println("资源池已空，消费者:"+Thread.currentThread().getId()+"进入等待状态");
                }catch (Exception e1){
                    System.out.println("资源池已空==>Exception,"+e1);
                }
            }
        }catch (Exception e){
            System.out.println("Exception,"+e);
        }finally {
            lock.unlock();
        }
    }

    public void add(){
        lock.lock();
        try {
            if(availableNum<initCapacity){
                availableNum++;
                System.out.println("生产者:"+Thread.currentThread().getId()+"生产了一条资源，当前资源数量："+this.availableNum);
                triggerConsumerCondition.signalAll();//唤醒等待的消费者
            }else {
                try{
                    triggerProducerCondition.await();
                    System.out.println("资源池已满，生产者:"+Thread.currentThread().getId()+"进入等待状态");
                }catch (Exception e1){
                    System.out.println("资源池已满==>Exception,"+e1);
                }
            }
        }catch (Exception e){
            System.out.println("Exception,"+e);
        }finally {
            lock.unlock();
        }
    }

}

/**
 * 生产者线程
 */
class ProducerB extends Thread{
    private Resource resource;
    public ProducerB(Resource resource){
        this.resource=resource;
    }

    public void run(){
        while (true){
            try {
                Thread.sleep((long) (1000 * Math.random()));
            }catch (Exception e){
                System.out.println("Exception,"+e);
            }
            resource.add();
        }
    }

}

/**
 * 消费者线程
 */
class ConsumerB extends Thread{
    private  Resource resource;
    public ConsumerB(Resource resource){
        this.resource=resource;
    }

    public void run(){
        while (true){
            try {
                Thread.sleep((long) (1000 * Math.random()));
            }catch (Exception e){
                System.out.println("Exception,"+e);
            }
            resource.remove();
        }
    }
}