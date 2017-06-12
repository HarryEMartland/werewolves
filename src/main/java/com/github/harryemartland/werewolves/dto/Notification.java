package com.github.harryemartland.werewolves.dto;

import lombok.Data;

@Data
public class Notification {

    private String title;
    private String body;
    private String type;

    /**
     * An event wich is displayed as a notification popup in the ui.
     * @param title the title of the notification popup
     * @param body the body of the notification popup
     * @param type the type of the notification popup (error, warning, success)
     */
    public Notification(String title, String body, String type) {
        this.title = title;
        this.body = body;
        this.type = type;
    }
}
