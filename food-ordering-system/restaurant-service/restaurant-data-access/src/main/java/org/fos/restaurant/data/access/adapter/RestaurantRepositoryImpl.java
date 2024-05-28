package org.fos.restaurant.data.access.adapter;

import lombok.RequiredArgsConstructor;
import org.fos.common.data.access.entity.RestaurantEntity;
import org.fos.common.data.access.repository.RestaurantJpaRepository;
import org.fos.restaurant.app.service.ports.output.repository.RestaurantRepository;
import org.fos.restaurant.data.access.mapper.RestaurantDataAccessMapper;
import org.fos.restaurant.domain.core.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {
    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

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
