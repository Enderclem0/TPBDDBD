package fr.uge;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.Closeable;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class AvroKafkaProducer<K,V> implements Closeable {
    private static final String TOPIC =  "students-avro";
    private final KafkaProducer<K,V> producer;



    public AvroKafkaProducer(Properties config) {
        Objects.requireNonNull(config);
        this.producer = new KafkaProducer<>(config);
    }

    public void send(V value) throws ExecutionException, InterruptedException {
        Objects.requireNonNull(value);
        producer.send(new ProducerRecord<>(TOPIC, value)).get();
    }

    @Override
    public void close() {
        this.producer.close();
    }
}
