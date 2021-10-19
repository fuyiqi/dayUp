package EngineSkill.Kafka;

import EngineSkill.Kafka.Consumer.KafkaConsumerDemo;
import EngineSkill.Kafka.Producer.KafkaProducerDemo;
import org.testng.annotations.Test;

public class Main {
    @Test
    public void test_producer(){
        KafkaProducerDemo kfk = KafkaProducerDemo.getInstance();
        kfk.send_sync("","fff");
    }


    @Test
    public void test_consumer(){
        KafkaConsumerDemo kfk = KafkaConsumerDemo.getInstance();
        kfk.get_at_most_once("");
    }


}
