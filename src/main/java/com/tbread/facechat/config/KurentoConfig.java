package com.tbread.facechat.config;

import com.tbread.facechat.kurento.CallHandler;
import com.tbread.facechat.kurento.RoomManager;
import com.tbread.facechat.kurento.UserRegistry;
import org.kurento.client.KurentoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
public class KurentoConfig implements WebSocketConfigurer {

    @Bean
    public UserRegistry registry() {
        return new UserRegistry();
    }

    @Bean
    public RoomManager roomManager() {
        return new RoomManager();
    }

    @Bean
    public CallHandler groupCallHandler() {
        return new CallHandler();
    }

    @Bean
    public KurentoClient kurentoClient() {
        return KurentoClient.create();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(groupCallHandler(), "/facechat");
    }
}
