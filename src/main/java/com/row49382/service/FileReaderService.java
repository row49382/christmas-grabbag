package com.row49382.service;

import java.io.IOException;

public interface FileReaderService<T> {
    T read() throws IOException;
}
