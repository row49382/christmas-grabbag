package com.row49382.service;

import com.row49382.exception.EmailServiceException;

public interface EmailingService {
    void sendEmail() throws EmailServiceException;
}
