package com.pratik.kafka.config;

import java.io.IOException;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class WeatherInfoService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final String[] cityNames = new String[] {"Delhi", "Mosco", "California", "Budapest", "Jakarta", "Paris", "Munich", "Berlin", "Singapore", "Newyork"};

    @Autowired
    private KafkaTemplate<String, WeatherInfoEvent> kafkaTemplate;

    public ListenableFuture<SendResult<String, WeatherInfoEvent>> sendMessage(String topic, WeatherInfoEvent message) {
         logger.info(String.format("#### -> Producing message -> %s", message));
         return this.kafkaTemplate.send(topic, message);
    }

    @Scheduled(fixedDelay = 5000)
    public void getWeatherInfoJob() throws IOException {
         logger.info("generate fake weather event");
         
         WeatherInfoEvent event = new WeatherInfoEvent(RandomUtils.nextLong(0, 100), RandomUtils.nextInt(0, 25), cityNames[RandomUtils.nextInt(0, 10)]);
         sendMessage("weather", event);
    }
}
