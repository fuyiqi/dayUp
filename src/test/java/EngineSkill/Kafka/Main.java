package EngineSkill.Kafka;

import EngineSkill.Kafka.Consumer.KafkaConsumerDemo;
import EngineSkill.Kafka.Producer.KafkaProducerDemo;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class Main {
    @Test
    public void test_producer() throws InterruptedException {
        KafkaProducerDemo kfk = KafkaProducerDemo.getInstance();
        while(true){
            TimeUnit.SECONDS.sleep(1);
            kfk.send_async("","fff");
        }

    }


    @Test
    public void test_consumer(){
        KafkaConsumerDemo kfk = KafkaConsumerDemo.getInstance();
        kfk.get_at_most_once("");
    }

    public static void main(String[] args){

    }
}
