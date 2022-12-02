package com.row49382.service;

import javax.mail.MessagingException;

public interface EmailingService {
    void sendEmail() throws MessagingException;
}
