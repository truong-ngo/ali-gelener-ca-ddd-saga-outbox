package org.fos.order.app.service.test;

import org.fos.order.app.service.ports.output.repository.CustomerRepository;
import org.fos.order.app.service.ports.output.repository.OrderRepository;
import org.fos.order.app.service.ports.output.repository.RestaurantRepository;
import org.fos.order.domain.core.OrderDomainService;
import org.fos.order.domain.core.OrderDomainServiceImpl;
import org.fos.order.app.service.ports.output.message.publisher.payment.OrderCancelledPaymentRequestMessagePublisher;
import org.fos.order.app.service.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMessagePublisher;
import org.fos.order.app.service.ports.output.message.publisher.restaurantapproval.OrderPaidRestaurantRequestMessagePublisher;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "org.fos.order.app.service")
public class OrderTestConfiguration {

    @Bean
    public OrderCreatedPaymentRequestMessagePublisher orderCreatedPaymentRequestMessagePublisher() {
        return Mockito.mock(OrderCreatedPaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderCancelledPaymentRequestMessagePublisher orderCancelledPaymentRequestMessagePublisher() {
        return Mockito.mock(OrderCancelledPaymentRequestMessagePublisher.class);
    }

    @Bean
    public OrderPaidRestaurantRequestMessagePublisher orderPaidRestaurantRequestMessagePublisher() {
        return Mockito.mock(OrderPaidRestaurantRequestMessagePublisher.class);
    }

    @Bean
    public OrderRepository orderRepository() {
        return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public CustomerRepository customerRepository() {
        return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    public RestaurantRepository restaurantRepository() {
        return Mockito.mock(RestaurantRepository.class);
    }

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }
}
