package EngineSkill.ProducerConsumer.A;

/**
 * 简单的生产者消费者模式
 */
public class Simple {


    public static void main(String[] args){
        /*************************************/
        Resource r1 = new Resource();

        Thread pa1 = new Thread(new ProducerA(r1));
        Thread pa2 = new Thread(new ProducerA(r1));


        Thread ca1 = new Thread(new ConsumerA(r1));

        pa1.start();pa2.start();
        ca1.start();
        /*************************************/

    }



}


/**
 * 公共资源
 */
class Resource{
    //当前资源数量
    private int currrentNum=0;
    //资源存放数量的上限
    private int capacity=20;
    //取走
    public synchronized void remove(){
        if(this.currrentNum>0){
            currrentNum--;
            System.out.println("消费者:"+Thread.currentThread().getName()+"消费了一件资源，当前资源数量："+this.currrentNum);
            notifyAll();//通知生产者去生产
        }else {
            try{
                wait();
                System.out.println("资源池已空，消费者:"+Thread.currentThread().getName()+"进入等待状态");
            }catch(Exception e){
                System.out.println("Exception,"+e);
            }
        }
    }

    //放入
    public synchronized void add(){
        if(this.currrentNum<this.capacity){
            currrentNum++;
            System.out.println("生产者:"+Thread.currentThread().getName()+"生产了一件资源，当前资源数量："+this.currrentNum);
            notifyAll();//通知消费者去消费
        }else {
            try{
                wait();
                System.out.println("资源池已满，生产者:"+Thread.currentThread().getName()+"进入等待状态");
            }catch(Exception e){
                System.out.println("Exception,"+e);
            }
        }
    }

}

/**
 * 生产者线程
 */
class ProducerA extends Thread{
    private Resource resource;
    public ProducerA(Resource resource){
        this.resource=resource;
    }

    public void run(){
        while (true){
            try {
                Thread.sleep(1000);
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
class ConsumerA extends Thread{
    private Resource resource;
    public ConsumerA(Resource resource){
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