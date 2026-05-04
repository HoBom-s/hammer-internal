package com.hammer.internal.common.domain;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String entity, Object id) {
        super(entity + " not found: " + id);
    }
}
