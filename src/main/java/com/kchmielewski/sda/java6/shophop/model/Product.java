package com.kchmielewski.sda.java6.shophop.model;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

public class Product {
    private final String name;
    private final BigDecimal price;

    public Product(String name, BigDecimal price) {
        checkArgument(name != null && name.trim().length() > 0, "Name cannot be null and cannot be empty: %s", name);
        checkArgument(price!= null && price.doubleValue() > 0, "Price cannot be null and must be positive: %s", price);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
