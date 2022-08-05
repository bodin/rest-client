package io.bodin.rest.model;

public class RestResponse<T> {
    private final int status;
    private Headers headers;
    private final T entity;

    public static <T> RestResponse<T> of(int status, Headers headers, T entity){
        return new RestResponse<>(status, headers, entity);
    }

    protected RestResponse(int status, Headers headers, T entity) {
        this.status = status;
        this.headers = headers;
        this.entity = entity;
    }

    public int getStatus() {
        return status;
    }

    public Headers getHeaders() {
        return headers;
    }

    public T getEntity() {
        return entity;
    }
}
