package com.celik.sheetstoslack.model;

public class SlackNotification {

    private String text;

    private String sheetName;

    private String channel;

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SlackNotification(String sheetName, String message, String userName, String channel) {
        this.sheetName = sheetName;
        this.text = message;
        this.channel = channel;
        this.userName = userName;
    }

    public String getText() {
        return "Hi, this is Crossover bootcamp sheet update notifier, there is a change on google sheet \'" +
                sheetName + "\':\n" + text;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
