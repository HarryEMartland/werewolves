package com.github.harryemartland.werewolves.contoller;

import com.github.harryemartland.werewolves.domain.NotificationException;
import com.github.harryemartland.werewolves.dto.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@Component
@ControllerAdvice
public class ControllerAdviceExceptionHandler {

    @MessageExceptionHandler
    @SendToUser(value = "/queue/notification", broadcast = false)
    public Notification handleException(Exception e) {
        log.error("Messaging error", e);
        return new Notification("Error", e.getMessage(), "error");
    }

    @MessageExceptionHandler
    @SendToUser(value = "/queue/notification", broadcast = false)
    public Notification handleException(NotificationException e) {
        return new Notification(e.getTitle(), e.getMessage(), e.getType());
    }
}
