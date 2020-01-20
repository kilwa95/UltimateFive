package com.example.ultimatefive;

/**
 * created by Abdulhalim Khaled on 2020-01-19.
 */
public class Message {

   private String from;
   private String message;
   private String type;

    public Message(String from, String message, String type) {
        this.from = from;
        this.message = message;
        this.type = type;
    }

    public Message()
    {

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
