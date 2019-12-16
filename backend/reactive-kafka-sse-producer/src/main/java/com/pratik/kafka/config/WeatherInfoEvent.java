package com.pratik.kafka.config;

import java.io.Serializable;

public class WeatherInfoEvent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -230771756739755344L;
	private String type = "[Kafka] Add message";
	private long stationId;
    private int temperature;
    private String city;
    
	public WeatherInfoEvent(long stationId, int temperature, String city) {
		this.stationId = stationId;
		this.temperature = temperature;
		this.city = city;
	}
	
	
	public WeatherInfoEvent() {
		super();
	}


	public long getStationId() {
		return stationId;
	}
	public void setStationId(long stationId) {
		this.stationId = stationId;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	@Override
	public String toString() {
		return "WeatherInfo [City=" + city + ", temperature=" + temperature + "]";
	}
    
    
}
