package EngineSkill.Kafka.Producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.errors.RetriableException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Future;

public class KafkaProducerDemo {

    private static Logger logger = LoggerFactory.getLogger(KafkaProducerDemo.class);
    private static final String defaultTopicName = "myKafkaTopic";
    private static KafkaProducer<String,String> producer = null;
    private static Properties kafkaConf = null;

    private static volatile KafkaProducerDemo instance = null;

    private KafkaProducerDemo(){
        kafkaConf = getKafkaConf();
        producer = new KafkaProducer<String, String>(kafkaConf);
    }

    private static Properties getKafkaConf(){
        Properties props = new Properties();
        //必有属性
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        //可选属性
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"producer");
        return props;
    }

    public static KafkaProducerDemo getInstance(){
        if(instance==null){
            synchronized (KafkaProducerDemo.class){
                if(instance==null){
                    instance = new KafkaProducerDemo();
                }
            }
        }
        return instance;
    }

    public synchronized static void send_then_forget(String customTopicName,String msg){
        boolean isCustomTopicBlank = customTopicName==null||customTopicName.length() == 0;
        if(isCustomTopicBlank){
            customTopicName = defaultTopicName;
        }
        try{
            ProducerRecord<String,String> record = new ProducerRecord<>(customTopicName,"send_then_forget",msg);
            producer.send(record);
        }catch (Exception e){
            logger.error("",e);
        }finally {
            //producer.flush();
            //producer.close();
        }
    }

    public synchronized static void send_async(String customTopicName,String msg){
        boolean isCustomTopicBlank = customTopicName==null||customTopicName.length() == 0;
        if(isCustomTopicBlank){
            customTopicName = defaultTopicName;
        }

        try{
            ProducerRecord<String,String> record = new ProducerRecord<>(customTopicName,"send_async",msg);
            producer.send(record, (recordMetadata, e) -> {
                boolean exception_is_null = e == null;
                if(!exception_is_null){
                    logger.error("push failed->",e);
                    if(e instanceof RetriableException){//可重试的失败

                    }else{

                    }
                }else {
                    if(null!=recordMetadata){
                        logger.info("topicName="+recordMetadata.topic()+";offset="+recordMetadata.offset()+";partition="+recordMetadata.partition());
                    }
                }
            });
        }catch (Exception e){
            logger.error("",e);
        }finally {
            //producer.flush();
            //producer.close();
        }
    }

    public synchronized static void send_sync(String customTopicName,String msg) {
        boolean isCustomTopicBlank = customTopicName == null || customTopicName.length() == 0;
        if (isCustomTopicBlank) {
            customTopicName = defaultTopicName;
        }

        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(customTopicName, "send_sync", msg);
            Future<RecordMetadata> recordMetadataFuture =  producer.send(record);
            producer.flush();
            //阻塞
            RecordMetadata recordMetadata = recordMetadataFuture.get();
            if(null!=recordMetadata){
                logger.info("topicName="+recordMetadata.topic()+";offset="+recordMetadata.offset()+";partition="+recordMetadata.partition());
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            //producer.flush();
            //producer.close();
        }

    }












}
