package com.kchmielewski.sda.java6.shophop.model;

import java.math.BigDecimal;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Order {
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private BigDecimal totalPriceIncludingPromotions = totalPrice;
    private Map<Product, Integer> products = new LinkedHashMap<>();


    public Map<Product, Integer> getProducts() {
        return Collections.unmodifiableMap(products);
//        return new LinkedHashMap<>(products);
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getTotalPriceIncludingPromotions() {
        return totalPriceIncludingPromotions;
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, int quantity) {
        checkNotNull(product, "Product cannot be null");
        checkArgument(quantity > 0, "Quantity must be positive: %s", quantity);

        if (products.containsKey(product)) {
            products.put(product, products.get(product) + quantity);
        } else {
            products.put(product, quantity);
        }

        calculateTotalPrice(product, quantity);
    }

    public BigDecimal recalculateWithPromotions(PriorityQueue<Promotion> promotions) {
        totalPriceIncludingPromotions = totalPrice;
        for (Promotion promotion: promotions) {
            if (promotion.isApplicable(this)) {
                totalPriceIncludingPromotions = promotion.apply(this);
            }
        }

        return totalPriceIncludingPromotions;
    }

    private void calculateTotalPrice(Product product, int quantity) {
        totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
    }

}