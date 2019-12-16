package com.example.demo;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

@Service
public class KafkaServiceImpl implements KafkaService {

    private Flux<ReceiverRecord<String, WeatherInfoEvent>> weatherTopicStream;

    KafkaServiceImpl() throws IOException {

        Map<String, Object> consumerProperties = new HashMap<>();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        consumerProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 2000);

        ReceiverOptions<String, WeatherInfoEvent> receiverOptions = ReceiverOptions.create(consumerProperties);
        weatherTopicStream = createTopicCache(receiverOptions, "weather");
    }

    public Flux<ReceiverRecord<String, WeatherInfoEvent>> getTestTopicFlux() {
        return weatherTopicStream;
    }

    private <T,G> Flux<ReceiverRecord<T,G>> createTopicCache(ReceiverOptions<T,G> receiverOptions, String topicName){
    	
        ReceiverOptions<T,G> options = receiverOptions.subscription(Collections.singleton(topicName));
        return KafkaReceiver.create(options).receive().cache();
    }
}