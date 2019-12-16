package com.pratik.kafka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class WeatherInfoController {

	@Autowired
	private WeatherInfoEventProcessor processor;

	private Flux<WeatherInfoEvent> bridge;

	public WeatherInfoController() {
		this.bridge = createBridge().publish().autoConnect().cache(10).log();
	}

	@GetMapping(value = "/weather", produces = "text/event-stream;charset=UTF-8")
	public Flux<WeatherInfoEvent> getWeatherInfo() {
		return bridge;
	}

	private Flux<WeatherInfoEvent> createBridge() {
		Flux<WeatherInfoEvent> bridge = Flux.create(sink -> {
			processor.register(new WeatherInfoEventListener() {

				@Override
				public void processComplete() {
					sink.complete();
				}

				@Override
				public void onData(WeatherInfoEvent data) {
					sink.next(data);
				}
			});
		});
		return bridge;
	}
}