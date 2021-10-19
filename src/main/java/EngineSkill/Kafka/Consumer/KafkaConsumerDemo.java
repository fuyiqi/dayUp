package EngineSkill.Kafka.Consumer;



import org.apache.kafka.clients.consumer.ConsumerConfig;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerDemo {

    private static Logger logger = LoggerFactory.getLogger(KafkaConsumerDemo.class);
    private static final String defaultTopicName = "myKafkaTopic";
    private static Properties kafkaConf = null;
    private static KafkaConsumer<String,String> consumer = null;

    private static Properties getKfkConsumer_CommonConf(){
        Properties props = new Properties();
        //必有属性
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG,"consumer2");
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"group_localhost2");
        return props;
    }

    private static void getKfkConsumer_AutoCommitConf(){
        kafkaConf.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        kafkaConf.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");
        kafkaConf.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");//latest,earliest
        kafkaConf.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"30000");
    }

    private KafkaConsumerDemo(){
        kafkaConf = getKfkConsumer_CommonConf();
        getKfkConsumer_AutoCommitConf();
        consumer = new KafkaConsumer<String, String>(kafkaConf);
    }

    private static class Holder{
        private static KafkaConsumerDemo instance = new KafkaConsumerDemo();
    }

    public static KafkaConsumerDemo getInstance(){
        return Holder.instance;
    }

    public synchronized static void get_at_most_once(String customTopicName){
        boolean isCustomTopicBlank = customTopicName==null||customTopicName.length() == 0;
        if(isCustomTopicBlank){
            customTopicName = defaultTopicName;
        }

        consumer.subscribe(Arrays.asList(customTopicName));//可订阅多个topic
        while (true){
            ConsumerRecords<String,String> records = consumer.poll(100);
            for(ConsumerRecord record:records){
                logger.info("topicName="+record.topic()+";offset="+record.offset()+";partition="+record.partition()
                        +";key="+record.key()+";value="+record.value());
            }
        }
    }

    public synchronized static void get_at_least_once(String customTopicName){
        //ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG 是false
        boolean isCustomTopicBlank = customTopicName==null||customTopicName.length() == 0;
        if(isCustomTopicBlank){
            customTopicName = defaultTopicName;
        }

        consumer.subscribe(Arrays.asList(customTopicName));//可订阅多个topic
        while (true){
            ConsumerRecords<String,String> records = consumer.poll(100);
            for(ConsumerRecord record:records){
                logger.info("topicName="+record.topic()+";offset="+record.offset()+";partition="+record.partition()
                        +";key="+record.key()+";value="+record.value());
            }
        }
    }






















}
