package com.row49382.domain;

public class Participant {
    private String name;
    private Participant receiver;
    private boolean isPicked;
    private String email;

    public Participant(String name, String email) {
        this.name = name;
        this.email = email;
        this.isPicked = false;
        this.receiver = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Participant getReceiver() {
        return receiver;
    }

    public void setReceiver(Participant receiver) {
        this.receiver = receiver;
    }

    public boolean isPicked() {
        return isPicked;
    }

    public void setPicked(boolean picked) {
        isPicked = picked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
