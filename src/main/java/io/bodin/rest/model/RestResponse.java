package io.bodin.rest.model;

public class RestResponse<T> {
    private final int status;
    private final T entity;

    public static <T> RestResponse<T> of(int status, T entity){
        return new RestResponse<>(status, entity);
    }

    protected RestResponse(int status, T entity) {
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
