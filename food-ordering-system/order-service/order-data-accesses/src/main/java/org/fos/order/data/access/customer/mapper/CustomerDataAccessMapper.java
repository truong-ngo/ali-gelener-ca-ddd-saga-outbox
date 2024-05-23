package org.fos.order.data.access.customer.mapper;

import org.fos.common.domain.valueobject.CustomerId;
import org.fos.order.data.access.customer.entity.CustomerEntity;
import org.fos.order.domain.core.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity entity) {
        Customer customer = new Customer();
        customer.setId(new CustomerId(entity.getId()));
        return customer;
    }

    public CustomerEntity customerToCustomerEntity(Customer customer) {
        return new CustomerEntity(customer.getId().getValue());
    }
}
