package fr.uge;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class TP2KafkaConsumer<K,V> implements Runnable, AutoCloseable {

    private static final String TOPIC =  "Etudiants5";
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private final String name;
    private final KafkaConsumer<K,V> consumer;
    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    public TP2KafkaConsumer(Properties config, String name) {
        Objects.requireNonNull(config);
        Objects.requireNonNull(name);
        this.name = name;
        this.consumer = new KafkaConsumer<>(config);
    }

    private void process(ConsumerRecord<K,V> record) {
        System.out.println(name+": "+record.value());
    }


    @Override
    public void run() {
        try {
            consumer.subscribe(Collections.singletonList(TOPIC));

            while (!shutdown.get()) {
                ConsumerRecords<K, V> records = consumer.poll(TIMEOUT);
                records.forEach(this::process);
            }
        } finally {
            consumer.close();
        }
    }

    public void shutdown() {
        shutdown.set(true);
    }

    @Override
    public void close() throws Exception {
        consumer.close();
    }
}
