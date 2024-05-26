package org.fos.restaurant.app.service.ports.output.repository;

import org.fos.restaurant.domain.core.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);
}
