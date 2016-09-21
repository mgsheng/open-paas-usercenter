package cn.com.open.openpaas.userservice.app.kafka;
import java.util.Properties;  

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.dev.UserserviceDev;
  
import kafka.javaapi.producer.Producer;  
import kafka.producer.KeyedMessage;  
import kafka.producer.ProducerConfig;  
import kafka.serializer.StringEncoder;  
@Service("kafkaProducer")
public class KafkaProducer{
	@Resource(name="userServiceDev")
	private UserserviceDev userserviceDev;
	private static Producer producer;
	
    public static Producer getProducer() {
		return producer;
	}

    public static void setProducer(Producer producer) {
		KafkaProducer.producer = producer;
	}


    public void sendMessage(String topic,String message){
    	if(producer==null)
    		producer=createProducer();
    	producer.send(new KeyedMessage<Integer, String>(topic,message));  
    }
    
    private synchronized Producer createProducer() {  
        Properties properties = new Properties();  
        properties.put("zookeeper.connect",userserviceDev.getZookeeper_connect());//声明zk  
        properties.put("serializer.class", StringEncoder.class.getName());  
   	    properties.put("metadata.broker.list",userserviceDev.getMetadata_broker_list());// 声明kafka broker  
        return new Producer<Integer, String>(new ProducerConfig(properties));  
     }  
   

}