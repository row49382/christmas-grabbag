package com.row49382.service;

import com.row49382.exception.PairingGenerateException;

public interface PairingGeneratable {
    void generate() throws PairingGenerateException;
}
