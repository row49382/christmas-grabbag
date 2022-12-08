package com.row49382.domain;

import java.util.Objects;

public class Participant {
    private final String name;
    private final String email;
    private Participant receiver;
    private boolean picked;

    public Participant(String name, String email) {
        this.name = name;
        this.email = email;
        this.receiver = null;
        this.picked = false;
    }

    public String getName() {
        return name;
    }

    public Participant getReceiver() {
        return receiver;
    }

    public void setReceiver(Participant receiver) {
        this.receiver = receiver;
    }

    public boolean isPicked() {
        return this.picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        String receiverName = this.getReceiver() != null ? this.getReceiver().getName() : "null";
        return String.format(
                "%s has %s",
                this.getName(),
                receiverName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Participant that = (Participant) o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.email);
    }
}
