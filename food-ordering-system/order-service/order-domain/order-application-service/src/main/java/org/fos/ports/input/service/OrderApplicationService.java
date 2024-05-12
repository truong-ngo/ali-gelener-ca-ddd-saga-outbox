package org.fos.ports.input.service;

import jakarta.validation.Valid;
import org.fos.dto.create.CreateOrderCommand;
import org.fos.dto.create.CreateOrderResponse;
import org.fos.dto.track.TrackOrderQuery;
import org.fos.dto.track.TrackOrderResponse;

public interface OrderApplicationService {
    CreateOrderResponse createOrder(@Valid CreateOrderCommand command);
    TrackOrderResponse trackOrder(@Valid TrackOrderQuery query);
}
