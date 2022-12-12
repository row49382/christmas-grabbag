package com.row49382.exception;

public class DriverExecutionException extends Exception {
    private static final long serialVersionUID = 1336536989887858210L;

    public DriverExecutionException() {
        super("An error was encountered during execution. Review the logs for more detail.");
    }
}
