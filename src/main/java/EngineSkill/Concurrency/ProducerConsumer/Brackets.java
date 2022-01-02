package EngineSkill.Concurrency.ProducerConsumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 装蛋的篮子
 */

public class Brackets {
    private static Logger logger = LoggerFactory.getLogger(Brackets.class);
    private int initialCapacity=20;
    private int occupiedCapacity=0;
    private LinkedList<Egg> lineQueue;

    private Lock lock;
    private Condition informHens;
    private Condition informChildren;

    public Brackets(Lock lock,Condition informHens,Condition informChildren){
        this.lineQueue=new LinkedList<Egg>();
        this.lock=lock;
        this.informChildren=informChildren;
        this.informHens=informHens;
    }

    public void put(Egg egg){
        lock.lock();
        try{
            if(occupiedCapacity<initialCapacity){
                lineQueue.offer(egg);
                occupiedCapacity++;
                logger.info("一颗"+egg.toString()+"【装入】篮子，当前已占用槽位量："+occupiedCapacity);
                informChildren.signalAll();
            }else {//篮子已满
                try {
                    informHens.await();
                    logger.info("篮子已满，母鸡："+Thread.currentThread().getId()+"进入休息");
                }catch (Exception e2){

                }
            }
        }catch (Exception e1){

        }finally {
            lock.unlock();
        }
    }


    public void fetch(){
        lock.lock();
        try {
            if(occupiedCapacity>0){
                Egg egg = lineQueue.poll();
                occupiedCapacity--;
                logger.info("一颗"+egg.toString()+"【移出】篮子，当前篮子已占用槽位数量："+occupiedCapacity);
                informHens.signalAll();
            }else {//篮子已空
                try{
                    informChildren.await();
                    logger.info("篮子已空，小孩："+Thread.currentThread().getId()+"进入休息");
                }catch (Exception e2){

                }
            }
        }catch (Exception e1){

        }finally {
            lock.unlock();
        }
    }








}
