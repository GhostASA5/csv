package com.example.project;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class listener {

    private final Service service;

    @EventListener(ApplicationReadyEvent.class)
    public void listen() {
        service.csv();
    }
}
