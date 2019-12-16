package com.pratik.kafka.config;

public interface WeatherInfoEventListener {
	void onData(WeatherInfoEvent event);
    void processComplete();
}
