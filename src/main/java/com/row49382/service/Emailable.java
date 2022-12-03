package com.row49382.service;

import com.row49382.exception.EmailServiceException;

public interface Emailable {
    void send() throws EmailServiceException;
}
