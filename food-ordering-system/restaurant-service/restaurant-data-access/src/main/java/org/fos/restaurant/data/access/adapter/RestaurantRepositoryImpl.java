package org.fos.restaurant.data.access.adapter;

import org.fos.common.data.access.entity.RestaurantEntity;
import org.fos.common.data.access.repository.RestaurantJpaRepository;
import org.fos.restaurant.app.service.ports.output.repository.RestaurantRepository;
import org.fos.restaurant.data.access.mapper.RestaurantDataAccessMapper;
import org.fos.restaurant.domain.core.entity.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RestaurantRepositoryImpl implements RestaurantRepository {
    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository,
                                    RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts =
                restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(),
                        restaurantProducts);
        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
