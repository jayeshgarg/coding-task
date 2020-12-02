package com.gargjayesh.codingexercise.creditsuisse.exception;

public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = -7891371801651279419L;

    private final int errorCode;

    public ApplicationException(final int errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ApplicationException(" + errorCode + "): " + super.getMessage();
    }
}
