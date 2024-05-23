package org.fos.order.app.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.order.app.service.dto.create.CreateOrderCommand;
import org.fos.order.app.service.dto.create.CreateOrderResponse;
import org.fos.order.app.service.dto.track.TrackOrderQuery;
import org.fos.order.app.service.dto.track.TrackOrderResponse;
import org.fos.order.app.service.ports.input.service.OrderApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand command) {
        log.info("Creating order for customer: {} at restaurant: {}", command.customerId(), command.restaurantId());
        CreateOrderResponse response = orderApplicationService.createOrder(command);
        log.info("Order is created with tracking id: {}", response.trackingId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> queryOrder(@PathVariable UUID trackingId) {
        TrackOrderResponse response = orderApplicationService.trackOrder(TrackOrderQuery.builder().trackingId(trackingId).build());
        log.info("Return order status with tracking id: {}", trackingId);
        return ResponseEntity.ok(response);
    }
}
