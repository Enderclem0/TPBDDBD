package fr.uge;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() throws UnknownHostException, InterruptedException {
        var config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("group.id", "foo");
        config.put("bootstrap.servers", "localhost:9092");
        config.put("acks", "all");
        config.put("key.serializer", StringSerializer.class.getName());
        config.put("key.deserializer", StringDeserializer.class.getName());
        //config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.serializer", "fr.uge.ProducedObjectSerializer");
        config.put("value.deserializer", "fr.uge.ProducedObjectDeserializer");
        config.put("json.payload.class", ProducedObject.class.getName());

        var consumerThread = Thread.ofPlatform().start(new TP2KafkaConsumer<>(config));
        /*var producerThread = Thread.ofPlatform().start(()-> {
            try (var producer = new TP2KafkaProducer<>(config)) {
                for (int i = 0; i < 500; i++) {
                    producer.send("TestMessage");
                    Thread.sleep(500);
                }
            } catch (ExecutionException | InterruptedException e) {
                System.out.println(e);
            }
        });
        */
        var producerJsonThread = Thread.ofPlatform().start(()-> {
            try (var producer = new TP2KafkaProducer<>(config)) {
                for (int i = 0; i < 500; i++) {
                    producer.send(new ProducedObject(
                            "Jean",
                            "Dupont",
                            21,
                            "IT"
                    ));
                    Thread.sleep(500);
                }
            } catch (ExecutionException | InterruptedException e) {
                System.out.println(e);
            }
        });
        consumerThread.join();
        //producerThread.join();
        producerJsonThread.join();
    }
}
