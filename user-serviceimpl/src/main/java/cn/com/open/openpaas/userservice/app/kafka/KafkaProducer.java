package cn.com.open.openpaas.userservice.app.kafka;
import java.util.Properties;  
import java.util.concurrent.TimeUnit;  

import cn.com.open.openpaas.userservice.dev.UserserviceDev;
  
import kafka.javaapi.producer.Producer;  
import kafka.producer.KeyedMessage;  
import kafka.producer.ProducerConfig;  
import kafka.serializer.StringEncoder;  
public class KafkaProducer extends Thread{
	private UserserviceDev userserviceDev;
	private String sendPayMsg;
    
    public KafkaProducer(String sendPayMsg,UserserviceDev userserviceDev){  
        super();  
        this.userserviceDev = userserviceDev;  
        this.sendPayMsg=sendPayMsg;
    }  
      
      
    @Override  
    public void run() {
    	  GlobalConfig glob=GlobalConfig.getInstance();
    	  Producer producer =glob.getProducer();
           int i=0;  
            producer.send(new KeyedMessage<Integer, String>(userserviceDev.getKafka_topic(),sendPayMsg));  
            try {  
                TimeUnit.SECONDS.sleep(1);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
        }  
    }  
  
    private Producer createProducer() {  
        Properties properties = new Properties();  
        properties.put("zookeeper.connect", "10.100.136.36:2181,10.100.136.37:2181,10.100.136.38:2181");//声明zk  
        properties.put("serializer.class", StringEncoder.class.getName());  
        properties.put("metadata.broker.list", "10.100.136.33:9092,10.100.136.34:9092,10.100.136.35:9092");// 声明kafka broker  
        return new Producer<Integer, String>(new ProducerConfig(properties));  
     }  
   

      
    public static void main(String[] args) {  
       // new KafkaProducer("test","11").start();// 使用kafka集群中创建好的主题 test   
          
    }  
}