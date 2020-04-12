package com.selepdf.buyaround.network.model;

public abstract class Response {

    private Status status;

    public Status getStatus() {
        return status;
    }

    public enum Status {
        OK,
        INTERNAL_ERROR,
        WRONG_PASSWORD,
        MISSING_PARAMETERS,
        WEAK_PASSWORD,
        EXISTING_EMAIL,
        INVALID_TOKEN,
    }
}