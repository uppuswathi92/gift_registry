package com.uppu.swathi_giftregistryproject;

//Model class for Events
public class Events {
    private int eventId;
    private String eventName;
    private String eventAddress;
    private String eventDate;

    //Constructor
    public Events(int eventId, String eventName, String eventAddress, String eventDate) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventAddress = eventAddress;
        this.eventDate = eventDate;
    }
    //Getters and setters
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
