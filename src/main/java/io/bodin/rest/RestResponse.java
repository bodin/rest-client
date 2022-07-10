package io.bodin.rest;

public class RestResponse<T> {
    private int status;
    private T entity;

    public RestResponse(int status, T entity) {
        this.status = status;
        this.entity = entity;
    }

    public int getStatus() {
        return status;
    }

    public T getEntity() {
        return entity;
    }
}
