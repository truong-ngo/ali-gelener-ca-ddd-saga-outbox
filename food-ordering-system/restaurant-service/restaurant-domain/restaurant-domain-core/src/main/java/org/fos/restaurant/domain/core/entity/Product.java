package org.fos.restaurant.domain.core.entity;

import org.fos.common.domain.entity.BaseEntity;
import org.fos.common.domain.valueobject.Money;
import org.fos.common.domain.valueobject.ProductId;

public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;
    private final int quantity;
    private boolean available;

    private Product(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setPrice(builder.price);
        this.quantity = builder.quantity;
        setAvailable(builder.available);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void updateWithConfirmedNamePriceAndAvailability(String name, Money price, boolean available) {
        setName(name);
        setPrice(price);
        setAvailable(available);
    }


    public static final class Builder {
        private ProductId id;
        private String name;
        private Money price;
        private int quantity;
        private boolean available;

        private Builder() {
        }

        public Builder id(ProductId val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder available(boolean val) {
            available = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
