package org.fos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fos.dto.create.CreateOrderCommand;
import org.fos.dto.create.CreateOrderResponse;
import org.fos.dto.track.TrackOrderQuery;
import org.fos.dto.track.TrackOrderResponse;
import org.fos.ports.input.service.OrderApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;
    private final OrderTrackCommandHandler trackCommandHandler;

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand command) {
        return null;
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery query) {
        return null;
    }
}
