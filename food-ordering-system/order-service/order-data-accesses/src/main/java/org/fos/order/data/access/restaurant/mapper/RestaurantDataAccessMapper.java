package org.fos.order.data.access.restaurant.mapper;

import org.fos.common.data.access.entity.RestaurantEntity;
import org.fos.common.data.access.exception.RestaurantDataAccessException;
import org.fos.common.domain.valueobject.Money;
import org.fos.common.domain.valueobject.ProductId;
import org.fos.common.domain.valueobject.RestaurantId;
import org.fos.order.domain.core.entity.Product;
import org.fos.order.domain.core.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProductIds(Restaurant restaurant) {
        return restaurant.getProducts().stream()
                .map(p -> p.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> entities) {
        RestaurantEntity entity = entities.stream()
                .findFirst()
                .orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found"));

        List<Product> restaurantProducts = entities.stream().map(r -> new Product(
                new ProductId(r.getProductId()), r.getProductName(), new Money(r.getProductPrice()))).toList();

        return Restaurant.builder()
                .id(new RestaurantId(entity.getRestaurantId()))
                .products(restaurantProducts)
                .active(entity.isRestaurantActive())
                .build();
    }
}
