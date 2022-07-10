package io.bodin.rest.contract;

public interface Immutable<T> {
    T copy();
}
