package org.fos.restaurant.container;

import org.fos.restaurant.domain.service.RestaurantDomainService;
import org.fos.restaurant.domain.service.RestaurantDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestaurantDomainService paymentDomainService() {
        return new RestaurantDomainServiceImpl();
    }
}
