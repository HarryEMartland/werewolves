package com.github.harryemartland.werewolves.domain;

public class NotificationException extends Exception {

    private final String title;
    private final String type;

    /**
     * Exception to create a formatted notification in the ui.
     * @param type the type of notification (error, warning, success)
     * @param title the tile to be shown in the notification popup
     * @param message the body of the notification popup
     */
    public NotificationException(String type, String title, String message) {
        super(message);
        this.type = type;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }
}
