package org.fos.order.data.access.restaurant.adapter;

import lombok.RequiredArgsConstructor;
import org.fos.order.app.service.ports.output.repository.RestaurantRepository;
import org.fos.order.data.access.restaurant.mapper.RestaurantDataAccessMapper;
import org.fos.order.data.access.restaurant.repository.RestaurantJpaRepository;
import org.fos.order.domain.core.entity.Restaurant;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;

    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        return restaurantJpaRepository.findByRestaurantIdAndProductIdIn(
                restaurant.getId().getValue(),
                restaurantDataAccessMapper.restaurantToRestaurantProductIds(restaurant))
                .map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
