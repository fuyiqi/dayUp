package EngineSkill.Concurrency.ProducerConsumer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HensAndChildren {
    private static Logger logger = LoggerFactory.getLogger(HensAndChildren.class);
    public static void main(String[] args){
        final Lock lock = new ReentrantLock();
        Condition informHens = lock.newCondition();
        Condition informChildren = lock.newCondition();
        Brackets bracket = new Brackets(lock,informHens,informChildren);

        Hens hen1 = new Hens(bracket);Hens hen2 = new Hens(bracket);Hens hen3 = new Hens(bracket);
        Children child1 = new Children(bracket);Children child2 = new Children(bracket);Children child3 = new Children(bracket);

        hen1.start();hen2.start();hen3.start();
        child1.start();//child2.start();child3.start();


    }

}

class Hens extends Thread{
    private Brackets bracket;
    public Hens(Brackets bracket){
        this.bracket=bracket;
    }


    public void run() {
        while (true){
            try {
                Thread.sleep((long) (1000*Math.random()));
                bracket.put(randomEgg());
            }catch (Exception e){
            }
        }
    }

    /**
     * 生成随机时间
     */
    public static String randomDate() {
        try {
            SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd");
            String beginDate="1970-01-02";String endDate = "2021-05-17";
            Date start = formatIn.parse(beginDate);// 构造开始日期
            Date end = formatIn.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 01.drl 月 01.drl 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long dateMills = random(start.getTime(), end.getTime());
            Date date = new Date(dateMills);
            SimpleDateFormat formatOut = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return formatOut.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }

    public static Egg randomEgg(){
        String dateStr = randomDate();
        int surviveM = (int)(100*Math.random());
        return new Egg(dateStr,surviveM);
    }

}



class Children extends Thread{
    private Brackets bracket;
    public Children(Brackets bracket){
        this.bracket=bracket;
    }
    public void run() {
        while (true){
            try{
                Thread.sleep((long) (1000*Math.random()));
                bracket.fetch();
            }catch (Exception e){

            }
        }
    }
}