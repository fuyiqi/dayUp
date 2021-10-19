package EngineSkill.ProducerConsumer.Instance;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 蛋类
 */
public class Egg {
    private static volatile AtomicInteger ID = new AtomicInteger(0);
    private String birthDate;
    private int surviveMonth;
    private int Id;

    public Egg(String birthDate,int surviveMonth){
        this.surviveMonth=surviveMonth;
        this.birthDate=birthDate;
        Id = ID.getAndIncrement();
    }


    public String toString(){
        return "蛋id:"+this.Id+",破蛋日期:"+this.birthDate+",保质期:"+this.surviveMonth;
    }

}
