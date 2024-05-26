package org.fos.restaurant.domain.core.exception;

import org.fos.common.domain.exception.DomainException;

public class RestaurantNotFoundException extends DomainException {

    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
