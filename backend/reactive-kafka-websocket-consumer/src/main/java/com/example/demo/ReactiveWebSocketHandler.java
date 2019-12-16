package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component()
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private static final ObjectMapper json = new ObjectMapper();

    @Autowired()
    KafkaService kafkaService;

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
    	System.out.println("inside handle");
        return webSocketSession.send(kafkaService.getTestTopicFlux().log().doOnNext(r -> r.receiverOffset().acknowledge() )
                .map(record -> {
                	System.out.println(record.value());
                    try {
                        return json.writeValueAsString(record.value());
                    } catch (JsonProcessingException e) {
                        return "Error while serializing to JSON";
                    }
                })
                .map(webSocketSession::textMessage))
                .and(webSocketSession.receive()
                        .map(WebSocketMessage::getPayload).log());
    }
}