package org.fos.order.app.service.ports.input.service;

import jakarta.validation.Valid;
import org.fos.order.app.service.dto.create.CreateOrderCommand;
import org.fos.order.app.service.dto.create.CreateOrderResponse;
import org.fos.order.app.service.dto.track.TrackOrderQuery;
import org.fos.order.app.service.dto.track.TrackOrderResponse;

public interface OrderApplicationService {
    CreateOrderResponse createOrder(@Valid CreateOrderCommand command);
    TrackOrderResponse trackOrder(@Valid TrackOrderQuery query);
}
