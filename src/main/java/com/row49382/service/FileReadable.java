package com.row49382.service;

import java.io.IOException;

public interface FileReadable<T> {
    T read() throws IOException;
}
