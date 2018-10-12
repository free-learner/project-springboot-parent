package com.personal.springboot.scheduled.start;

import org.springframework.boot.ExitCodeGenerator;

public class MyExitException extends RuntimeException implements ExitCodeGenerator {
    private static final long serialVersionUID = 1L;

    @Override
    public int getExitCode() {
        return 10;
    }
}