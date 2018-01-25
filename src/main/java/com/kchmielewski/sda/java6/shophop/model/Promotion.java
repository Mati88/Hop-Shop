package com.kchmielewski.sda.java6.shophop.model;

import java.math.BigDecimal;
import java.util.function.Function;

public interface Promotion extends Function<Order, BigDecimal>, Comparable<Promotion> {
    boolean isApplicable(Order order);
}
