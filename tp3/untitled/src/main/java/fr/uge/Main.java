package fr.uge;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import fr.uge.avro.Student;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Main {
    static void main() throws UnknownHostException, InterruptedException {
        var config = getProperties();
        var builder = new StreamsBuilder();
        try(var studentSerde = new SerdeAvro(); final var stringSerde = Serdes.String()) {
                var studentKStream = builder.stream(
                        "students-avro",
                        Consumed.with(stringSerde, studentSerde)
                );
                studentKStream.foreach((_,value) -> System.out.println(value));
                var newStream = studentKStream.filter((_,v) -> v.getAge() > 20 && v.getEngineeringDegree().toString().equals("IT"));
                newStream.to("students-processed", Produced.with(stringSerde, studentSerde));

            var random = new Random();
            try(var streams = new KafkaStreams(builder.build(), config)) {
                streams.start();
                var producerJsonThread = Thread.ofPlatform().start(() -> {
                    try (var producer = new AvroKafkaProducer<>(config)) {
                        for (int i = 0; i < 500; i++) {
                            producer.send(new Student(
                                    "Jean",
                                    "Dupont",
                                    random.nextInt(18, 99),
                                    random.nextBoolean() ? "IT" : "EISC"
                            ));
                            Thread.sleep(500);
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        System.out.println(e);
                    }
                });

                producerJsonThread.join();
            }
        }
    }

    private static Properties getProperties() throws UnknownHostException {
        var config = new Properties();
        config.put("client.id", InetAddress.getLocalHost().getHostName());
        config.put("group.id", "foo");
        config.put("bootstrap.servers", "localhost:9092");
        config.put("acks", "all");
        config.put("key.serializer", StringSerializer.class.getName());
        config.put("key.deserializer", StringDeserializer.class.getName());
//        config.put("value.serializer", KafkaAvroSerializer.class.getName());
//        config.put("value.deserializer", KafkaAvroSerializer.class.getName());
        config.put("value.serializer", CustomStudentSerializer.class.getName());
        config.put("value.deserializer", CustomStudentDeserializer.class.getName());
        config.put("json.payload.class", Student.class.getName());
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "student-stream-app");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SerdeAvro.class);

        return config;
    }
}