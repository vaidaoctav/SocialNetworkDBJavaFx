package com.example.mysocialnetworkdb.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message extends Entity<Integer>{
    private int id_user1;
    private int id_user2;
    private int id_sender;
    private String message;
    private LocalDateTime dateTime;

    public Message(int id,int id_user1, int id_user2, int id_sender, String message, LocalDateTime dateTime) {
        super.setId(id);
        this.id_user1 = id_user1;
        this.id_user2 = id_user2;
        this.id_sender = id_sender;
        this.message = message;
        this.dateTime = dateTime;
    }

    public int getId_user1() {
        return id_user1;
    }

    public void setId_user1(int id_user1) {
        this.id_user1 = id_user1;
    }

    public int getId_user2() {
        return id_user2;
    }

    public void setId_user2(int id_user2) {
        this.id_user2 = id_user2;
    }

    public int getId_sender() {
        return id_sender;
    }

    public void setId_sender(int id_sender) {
        this.id_sender = id_sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message1)) return false;
        return getId_user1() == message1.getId_user1() && getId_user2() == message1.getId_user2() && getId_sender() == message1.getId_sender() && Objects.equals(getMessage(), message1.getMessage()) && Objects.equals(getDateTime(), message1.getDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_user1(), getId_user2(), getId_sender(), getMessage(), getDateTime());
    }
}
