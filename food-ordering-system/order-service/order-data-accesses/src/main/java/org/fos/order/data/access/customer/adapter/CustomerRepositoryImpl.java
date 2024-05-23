package org.fos.order.data.access.customer.adapter;

import lombok.RequiredArgsConstructor;
import org.fos.order.app.service.ports.output.repository.CustomerRepository;
import org.fos.order.data.access.customer.mapper.CustomerDataAccessMapper;
import org.fos.order.data.access.customer.repository.CustomerJpaRepository;
import org.fos.order.domain.core.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    private final CustomerDataAccessMapper customerDataAccessMapper;

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJpaRepository
                .findById(customerId)
                .map(customerDataAccessMapper::customerEntityToCustomer);
    }
}
