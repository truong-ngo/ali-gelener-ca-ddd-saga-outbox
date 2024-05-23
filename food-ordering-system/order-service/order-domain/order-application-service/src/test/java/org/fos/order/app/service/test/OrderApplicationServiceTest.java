package org.fos.order.app.service.test;

import org.fos.order.app.service.dto.create.CreateOrderCommand;
import org.fos.order.app.service.dto.create.OrderAddress;
import org.fos.order.app.service.dto.create.OrderItem;
import org.fos.order.app.service.ports.output.repository.CustomerRepository;
import org.fos.order.app.service.ports.output.repository.OrderRepository;
import org.fos.order.app.service.ports.output.repository.RestaurantRepository;
import org.fos.common.domain.valueobject.*;
import org.fos.order.app.service.dto.create.CreateOrderResponse;
import org.fos.order.app.service.mapper.OrderDataMapper;
import org.fos.order.domain.core.entity.Customer;
import org.fos.order.domain.core.entity.Order;
import org.fos.order.domain.core.entity.Product;
import org.fos.order.domain.core.entity.Restaurant;
import org.fos.order.domain.core.exception.OrderDomainException;
import org.fos.order.app.service.ports.input.service.OrderApplicationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;
    private final UUID CUSTOMER_ID = UUID.fromString("0009d05d-8bc3-494d-b443-5768a3a3e3f6");
    private final UUID RESTAURANT_ID = UUID.fromString("00cb7e92-306f-493a-82aa-6105d610bf42");
    private final UUID PRODUCT_ID = UUID.fromString("019adf0d-4180-4a95-8624-bfdcc2cd36d5");
    private final UUID ORDER_ID = UUID.fromString("02a20886-949e-4919-943b-089b1d219bed");
    private final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeAll
    public void init() {
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_37A")
                        .postalCode("1000AB")
                        .city("Ha Noi")
                        .build())
                .price(PRICE)
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();

        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_37A")
                        .postalCode("1000AB")
                        .city("Ha Noi")
                        .build())
                .price(new BigDecimal("250.00"))
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_37A")
                        .postalCode("1000AB")
                        .city("Ha Noi")
                        .build())
                .price(new BigDecimal("210.00"))
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("60.00"))
                                .subTotal(new BigDecimal("60.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurant = Restaurant.builder()
                .id(new RestaurantId(RESTAURANT_ID))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))
                ))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        Mockito.when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        Mockito.when(restaurantRepository.findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand))).thenReturn(Optional.of(restaurant));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
    }

    @Test
    public void testCreateOrder() {
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        Assertions.assertEquals(OrderStatus.PENDING, createOrderResponse.status());
        Assertions.assertEquals("Order Created Successfully", createOrderResponse.message());
        Assertions.assertNotNull(createOrderResponse.trackingId());
    }

    @Test
    public void testCreateOrderWithWrongOrderPrice() {
        OrderDomainException exception = Assertions.assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        Assertions.assertEquals("Total price: 250.00 is not equal to Order items total: 200.00!", exception.getMessage());
    }

    @Test
    public void testCreateOrderWithWrongProductPrice() {
        OrderDomainException exception = Assertions.assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
        Assertions.assertEquals("OrderItem price: 60.00 is not valid for product " + PRODUCT_ID, exception.getMessage());
    }

    @Test
    public void testCreateOrderWithPassiveRestaurant() {
        Restaurant restaurant = Restaurant.builder()
                .id(new RestaurantId(RESTAURANT_ID))
                .products(List.of(
                        new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))
                ))
                .active(false)
                .build();

        Mockito.when(restaurantRepository.findRestaurantInformation(restaurant)).thenReturn(Optional.of(restaurant));

        OrderDomainException exception = Assertions.assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommand));
        Assertions.assertEquals("Restaurant with id: " + RESTAURANT_ID + " is currently not active", exception.getMessage());
    }
}
