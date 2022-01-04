package EngineSkill.Concurrency;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HensAndMen {
    private static final Logger log= LoggerFactory.getLogger(HensAndMen.class);

    public static void main(String[] args) {
        final Lock lock = new ReentrantLock();
        Condition hensCondition = lock.newCondition();
        Condition menCondition = lock.newCondition();
        Basket basket = new Basket(lock,hensCondition,menCondition);
        for(int i=0;i<5;i++){//5个人拿蛋
            new Men(basket).start();
        }
        for(int i=0;i<5;i++){//5个鸡生蛋
            new Hens(basket).start();
        }

    }

    static class Men extends Thread{
        private final Basket basket;
        public Men(Basket basket){
            this.basket = basket;
        }

        public void run(){
            while (true){
                try {
                    Thread.sleep((long) (1000*Math.random()));
                    basket.remove();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Hens extends Thread{
        private final Basket basket;
        public Hens(Basket basket){
            this.basket = basket;
        }

        public void run(){
            while(true){
                try {
                    Thread.sleep((long) (1000*Math.random()));
                    basket.add();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    static class Basket{
        private final int designCapacity = 100;
        private final List<Egg> basket = Collections.synchronizedList(new ArrayList<>(designCapacity));
        private final Lock lock ;
        private final Condition hensCondition ;
        private final Condition menCondition ;

        public Basket(Lock lock,Condition hensCondition,Condition menCondition){
            this.lock = lock;
            this.hensCondition = hensCondition;
            this.menCondition = menCondition;
        }

        public static List<Egg> createEggs(int num){
            List<Egg> res = new ArrayList<>();
            for(int i=0;i<num;i++){
                String eggName = RandomStringUtils.randomAlphanumeric(2);
                res.add(new Egg(eggName));
            }
            return res;
        }

        private void add() throws InterruptedException {
            lock.lock();
            int eggNum = new Random().nextInt(100);
            if(basket.size()+eggNum>=designCapacity){//
                log.info("篮子里装不下要放的蛋,要放入{}个，当前可用位置数{}",eggNum,designCapacity-basket.size());
                menCondition.signalAll();
                log.info("通知人拿蛋");
                hensCondition.await();
                log.info("通知鸡别生蛋");
            }else{
                log.info("[放蛋]:{},当前可用位置数:{}",eggNum,designCapacity-basket.size());
                List<Egg> addEggs = createEggs(eggNum);
                basket.addAll(addEggs);
            }
            lock.unlock();
        }

        private void remove() throws InterruptedException {
            lock.lock();
            int removeNum = 0;
            if(basket.size()<=1){
                removeNum = 1;
            }else{
                removeNum = new Random().nextInt(basket.size());
            }
            if(basket.size()-removeNum<0){
                log.info("篮子里蛋的数量不够拿,要拿走{}个，当前可用蛋数量{}",removeNum,basket.size());
                hensCondition.signalAll();
                log.info("通知鸡生蛋");
                menCondition.await();
                log.info("通知人别拿蛋");
            }else{
                for(int i=0;i<removeNum;i++) {
                    basket.remove(0);
                }
                log.info("[拿蛋]:-{},当前蛋数:{}",removeNum,basket.size());
            }
            lock.unlock();
        }
    }

    static class Egg{
        private final String name;
        public Egg(String name){
            this.name=name;
        }

    }










}
