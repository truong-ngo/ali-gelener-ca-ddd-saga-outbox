package org.fos.restaurant.app.service.exception;

import org.fos.common.domain.exception.DomainException;

public class RestaurantApplicationServiceException extends DomainException {

    public RestaurantApplicationServiceException(String message) {
        super(message);
    }

    public RestaurantApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
