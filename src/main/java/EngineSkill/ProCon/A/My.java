package EngineSkill.ProCon.A;


import java.util.LinkedList;

public class My {

    public static void main(String[] args){

        LineResource lineResource = new LineResource();

        MyProducerA mpa1 = new MyProducerA(lineResource);MyProducerA mpa2 = new MyProducerA(lineResource);
        MyConsumerA mca1 = new MyConsumerA(lineResource);

        //mca1.start();
        mpa1.start();mpa2.start();
    }




}

/***公共资源池***/

class LineResource{
    private int currentNum=0;
    private int capacity=20;
    private static LinkedList<Integer> lineQueue;

    public synchronized void remove(){
        if(this.currentNum>0){
            currentNum--;
            Integer removeValue = lineQueue.poll(); //poll真移头，peek假移头
            System.out.println("消费者:"+Thread.currentThread().getId()+"取走资源值:"+removeValue+"，当前资源数量:"+this.currentNum);
            notifyAll();//通知生产者去生产
        }else {
            try{
                wait();
                System.out.println("当前资源数量为0，消费者:"+Thread.currentThread().getName()+"进入等待状态");
            }catch(Exception e){
                System.out.println("Exception,"+e);
            }
        }
    }

    public synchronized void add(Integer elem){
        if(this.currentNum<this.capacity){
            currentNum++;
            lineQueue.offer(elem);
            System.out.println("生产者:"+Thread.currentThread().getName()+"放入资源值:"+elem+"，当前资源数量:"+this.currentNum);
            notifyAll();//通知消费者去消费
        }else {
            try{
                wait();
                System.out.println("当前资源数量达上限，生产者:"+Thread.currentThread().getName()+"进入等待状态");
            }catch(Exception e){
                System.out.println("Exception,"+e);
            }
        }
    }


}

class CircleResource{

}


/**
 * 消费者线程
 */
class MyConsumerA extends Thread{
    private LineResource resource;
    public MyConsumerA(LineResource resource){
        this.resource=resource;
    }

    public void run(){
        while (true){
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                System.out.println("Exception,"+e);
            }
            resource.remove();
        }
    }
}


/**
 * 生产者线程
 */
class MyProducerA extends Thread{
    private LineResource resource;
    public MyProducerA(LineResource resource){
        this.resource=resource;
    }

    public void run(){
        int cnt=0;//随机数
        while (true){
            try {
                Thread.sleep(1000);
            }catch (Exception e){
                System.out.println("Exception,"+e);
            }
            resource.add(cnt++);
        }
    }

}