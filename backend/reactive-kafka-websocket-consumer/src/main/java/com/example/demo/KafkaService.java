package com.example.demo;

import reactor.core.publisher.Flux;
import reactor.kafka.receiver.ReceiverRecord;

public interface KafkaService {
	public Flux<ReceiverRecord<String,WeatherInfoEvent>> getTestTopicFlux();
}
